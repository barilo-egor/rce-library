package tgb.btc.library.service.web.merchant.nicepay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayMethod;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.vo.web.merchant.nicepay.CreateOrderRequest;
import tgb.btc.library.vo.web.merchant.nicepay.CreateOrderResponse;
import tgb.btc.library.vo.web.merchant.nicepay.GetOrderRequest;
import tgb.btc.library.vo.web.merchant.nicepay.GetOrderResponse;

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

    public NicePayMerchantService(@Value("${nicepay.api.merchantid:null}") String merchantId,
                                  @Value("${nicepay.api.secret:null}") String secret,
                                  @Value("${bot.name}") String botName,
                                  @Value("${nicepay.api.url:null}") String baseUrl,
                                  RestTemplate restTemplate) {
        this.merchantId = merchantId;
        this.secret = secret;
        this.botName = botName;
        this.restTemplate = restTemplate;
        this.createUrl = baseUrl + "/h2hOneRequestPayment";
        this.getUrl = baseUrl + "/h2hPaymentInfo";
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

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.NICE_PAY;
    }
}
