package tgb.btc.library.service.web.merchant.payfinity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityOrderType;
import tgb.btc.library.constants.enums.web.merchant.payfinity.PayFinityStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.payfinity.CreateOrderRequest;
import tgb.btc.library.vo.web.merchant.payfinity.CreateOrderResponse;
import tgb.btc.library.vo.web.merchant.payfinity.GetTransactionResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Objects;

@Service
public class PayFinityMerchantService implements IMerchantService {

    private final String privateKey;

    private final String publicKey;

    private final String getTransactionUrl;

    private final String getTransactionEndUrl;

    private final String createTransactionUrl;

    private final String createTransactionEndUrl;

    private final RestTemplate restTemplate;

    private final String botName;

    private final String callbackUrl;

    private final IReadDealService readDealService;

    private final ModifyDealRepository modifyDealRepository;

    private final INotifier notifier;

    public PayFinityMerchantService(@Value("${payfinity.api.url:null}") String url,
                                    @Value("${payfinity.api.key.public:null}") String publicKey,
                                    @Value("${payfinity.api.key.private:null}") String privateKey,
                                    @Value("${bot.name}") String botName,
                                    @Value("${main.url:null}") String mainUrl,IReadDealService readDealService,
                                    ModifyDealRepository modifyDealRepository, INotifier notifier) {
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.createTransactionEndUrl = "/api/v1/payment";
        this.createTransactionUrl = url + createTransactionEndUrl;
        this.getTransactionEndUrl = "/api/v1/account/transaction";
        this.getTransactionUrl = url + getTransactionEndUrl;
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(Duration.ofSeconds(2));
        simpleClientHttpRequestFactory.setReadTimeout(Duration.ofSeconds(3));
        this.restTemplate = new RestTemplate(simpleClientHttpRequestFactory);
        this.botName = botName;
        this.callbackUrl = mainUrl + "/merchant/payfinity";
        this.readDealService = readDealService;
        this.modifyDealRepository = modifyDealRepository;
        this.notifier = notifier;
    }

    public String getSign(String method, String params, String url, String body, String exp)
            throws Exception {
        String message = url;

        if (method.equals("GET")) {
            message += params;
        } else {
            if (body != null) {
                message += body;
            }
        }
        return generateSignature(message + exp);
    }

    private String generateSignature(String message)
            throws NoSuchAlgorithmException, InvalidKeyException {
        // Инициализация HMAC-SHA512
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(
                privateKey.getBytes(StandardCharsets.UTF_8),
                "HmacSHA512"
        );
        mac.init(secretKeySpec);

        // Вычисление HMAC
        byte[] hmacBytes = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        // Преобразование в шестнадцатеричную строку
        StringBuilder result = new StringBuilder();
        for (byte b : hmacBytes) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }

    public CreateOrderResponse createOrder(Deal deal) throws Exception {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        PayFinityOrderType payFinityOrderType = deal.getPaymentType().getPayFinityOrderType();
        createOrderRequest.setBank(payFinityOrderType.getBank());
        createOrderRequest.setType(payFinityOrderType.getType());
        createOrderRequest.setClientId(botName + deal.getPid());
        createOrderRequest.setCurrency("RUB");
        createOrderRequest.setCallbackUrl(callbackUrl);
        createOrderRequest.setAmount(deal.getAmount().toPlainString());
        String body = JacksonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(createOrderRequest);
        long expires = System.currentTimeMillis() + 300000L;
        HttpEntity<String> httpEntity = new HttpEntity<>(body, getHeaders(expires, "POST", createTransactionEndUrl, null, body));
        ResponseEntity<CreateOrderResponse> response = restTemplate.exchange(createTransactionUrl, HttpMethod.POST, httpEntity, CreateOrderResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании транзакции пустое.");
        }
        return response.getBody();
    }

    public PayFinityStatus getStatus(String trackerId) throws Exception {
        String params = "trackerId=" + trackerId;
        long expires = System.currentTimeMillis() + 300000L;
        HttpHeaders httpHeaders = getHeaders(expires, "GET", getTransactionEndUrl, params, null);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<GetTransactionResponse> response =
                restTemplate.exchange(getTransactionUrl + "?" + params, HttpMethod.GET, httpEntity, GetTransactionResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении статуса пустое.");
        }
        return response.getBody().getData().getStatus();
    }

    private HttpHeaders getHeaders(long expires, String method, String url, String params, String body) throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Public-Key", publicKey);
        String exp = String.valueOf(expires);
        httpHeaders.add("Expires", exp);
        httpHeaders.add("Signature", getSign(method, params, url, body, exp));
        return httpHeaders;
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    public void updateStatus(String trackerId) {
        try {
            Deal deal = readDealService.getByMerchantOrderId(trackerId);
            if (Objects.isNull(deal)) return;
            PayFinityStatus payFinityStatus = getStatus(trackerId);
            deal.setMerchantOrderStatus(payFinityStatus.name());
            modifyDealRepository.save(deal);
            if (!DealStatus.NEW.equals(deal.getDealStatus())) {
                notifier.merchantUpdateStatus(deal.getPid(), "PayFinity обновил статус по сделке №" + deal.getPid()
                        + " до \"" + payFinityStatus.getDescription() + "\".");
            }
        } catch (Exception e) {
            throw new BaseException("Не удалось обновить статус по транзакции " + trackerId);
        }

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.PAY_FINITY;
    }
}
