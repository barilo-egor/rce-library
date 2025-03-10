package tgb.btc.library.service.web.merchant.alfateam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.web.merchant.alfateam.AlfaTeamDealStatus;
import tgb.btc.library.constants.enums.web.merchant.alfateam.DirectionType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.alfateam.CreateInvoiceRequest;
import tgb.btc.library.vo.web.merchant.alfateam.CreateInvoiceResponse;
import tgb.btc.library.vo.web.merchant.alfateam.InvoiceNotification;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;

@Service
public class AlfaTeamMerchantService {

    private final RestTemplate restTemplate;

    private final String mainUrl;

    private final String notificationUrl;

    private final String notificationToken;

    private final String botName;

    private final String apiKey;

    private final String apiSecret;

    private final String createInvoiceUrl;

    private final IReadDealService readDealService;

    private final INotifier notifier;

    public AlfaTeamMerchantService(RestTemplate restTemplate, @Value("${main.url:null}") String mainUrl,
                                   @Value("${alfateam.api.notification.token:null}") String alfaTeamNotificationToken,
                                   @Value("${bot.name:null}") String botName, @Value ("${alfateam.api.key:null}") String apiKey,
                                   @Value ("${alfateam.api.secret:null}") String apiSecret,
                                   @Value("${alfateam.api.url.main:null}") String apiMainUrl, IReadDealService readDealService,
                                   INotifier notifier) {
        this.mainUrl = mainUrl;
        this.notificationUrl = mainUrl + "/merchant/alfateam";
        this.notificationToken = alfaTeamNotificationToken;
        this.botName = botName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.restTemplate = restTemplate;
        this.createInvoiceUrl = apiMainUrl + "/api/merchant/invoices";
        this.readDealService = readDealService;
        this.notifier = notifier;
    }


    public CreateInvoiceResponse createInvoice(Deal deal) throws Exception {
        CreateInvoiceRequest createInvoiceRequest = CreateInvoiceRequest.builder()
                .type(DirectionType.IN)
                .amount(deal.getAmount().toString())
                .currency(FiatCurrency.RUB.name())
                .notificationUrl(notificationUrl)
                .notificationToken(notificationToken)
                .internalId(botName + deal.getPid())
                .userId(deal.getUser().getChatId().toString())
                .paymentOption(deal.getPaymentType().getAlfaTeamPaymentOption())
                .startDeal(true)
                .build();
        String body = JacksonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(createInvoiceRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-Identity", apiKey);
        headers.add("X-Signature", generateXSignature("POST", createInvoiceUrl, body));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<CreateInvoiceResponse> response = restTemplate.exchange(createInvoiceUrl, HttpMethod.POST, entity, CreateInvoiceResponse.class);
        if (response.getBody() != null) {
            return response.getBody();
        }
        throw new BaseException("Тело ответа на создание инвойса пустое.");
    }

    public String generateXSignature(String method, String url, String body) throws Exception {
        String data = method.toUpperCase() + url + (body != null ? body : "");

        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA1"));
        byte[] rawHmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(rawHmac);
    }

    public void updateStatus(InvoiceNotification invoiceNotification) {
        Deal deal = readDealService.getByAlfaTeamInvoiceId(invoiceNotification.getInvoice().getId());
        if (Objects.isNull(deal)) return;
        AlfaTeamDealStatus status = invoiceNotification.getInvoice().getStatus();
        deal.setAlfaTeamDealStatus(status);
        if (!DealStatus.NEW.equals(deal.getDealStatus())) {
            notifier.merchantUpdateStatus(deal.getPid(), "AlfaTeam обновил статус по сделке №" + deal.getPid()
                    + " до \"" + status.getDescription() + "\".");
        }
    }
}
