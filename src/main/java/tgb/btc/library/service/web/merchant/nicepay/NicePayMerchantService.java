package tgb.btc.library.service.web.merchant.nicepay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayMethod;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.vo.web.merchant.nicepay.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Objects;

@Service
public class NicePayMerchantService implements IMerchantService {

    private final String merchantId;

    private final String secret;

    private final String botName;

    private final RestTemplate restTemplate;

    private final String createUrl;

    private final String getUrl;

    private final String paidUrl;

    private final String proofUrl;

    public NicePayMerchantService(@Value("${nicePay.api.merchantId:null}") String merchantId,
                                  @Value("${nicePay.api.secret:null}") String secret,
                                  @Value("${nicePay.api.url:null}") String baseUrl,
                                  @Value("${bot.name}") String botName,
                                  RestTemplate restTemplate) {
        this.merchantId = merchantId;
        this.secret = secret;
        this.botName = botName;
        this.restTemplate = restTemplate;
        this.createUrl = baseUrl + "/h2hOneRequestPayment";
        this.getUrl = baseUrl + "/h2hPaymentInfo";
        this.proofUrl = baseUrl + "/h2hUploadProof";
        this.paidUrl = baseUrl + "/h2hConfirmPaid";
    }

    public CreateOrderResponse createOrder(Deal deal) {
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setMerchantId(merchantId);
        createOrderRequest.setSecret(secret);
        createOrderRequest.setOrderId(botName + deal.getPid());
        createOrderRequest.setAmount(deal.getAmount().multiply(new BigDecimal(100)).intValue());
        createOrderRequest.setCustomer(deal.getUser().getChatId().toString());
        NicePayMethod nicePayMethod = deal.getPaymentType().getNicePayMethod();
        createOrderRequest.setMethod(nicePayMethod);
        switch (nicePayMethod) {
            case SBP_RU -> createOrderRequest.setMethodSBP("onlyRU");
            case SBP_TRANSGRAN -> createOrderRequest.setMethodSBP("onlyINT");
        }
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<CreateOrderRequest> httpEntity = new HttpEntity<>(createOrderRequest, httpHeaders);
        ResponseEntity<CreateOrderResponse> response = restTemplate.exchange(createUrl, HttpMethod.POST, httpEntity, CreateOrderResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении реквизитов пустое.");
        }
        return response.getBody();
    }

    public GetOrderResponse getOrder(String id) {
        GetOrderRequest getOrderRequest = new GetOrderRequest();
        getOrderRequest.setMerchantId(merchantId);
        getOrderRequest.setSecret(secret);
        getOrderRequest.setPayment(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<GetOrderRequest> httpEntity = new HttpEntity<>(getOrderRequest, httpHeaders);
        ResponseEntity<GetOrderResponse> response = restTemplate.exchange(getUrl, HttpMethod.POST, httpEntity, GetOrderResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении реквизитов пустое.");
        }
        return response.getBody();
    }

    public void paid(String id) {
        PaidRequest paidRequest = new PaidRequest();
        paidRequest.setSecret(secret);
        paidRequest.setSecret(merchantId);
        paidRequest.setPayment(id);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<PaidRequest> httpEntity = new HttpEntity<>(paidRequest, httpHeaders);
        restTemplate.exchange(getUrl, HttpMethod.POST, httpEntity, String.class);
    }

    public void uploadCheck(File file, String paymentId) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("merchant_id", merchantId);
        body.add("secret", secret);
        body.add("payment", paymentId);
        body.add("proof", new FileSystemResource(file));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        restTemplate.postForObject(proofUrl, requestEntity, String.class);
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.NICE_PAY;
    }
}
