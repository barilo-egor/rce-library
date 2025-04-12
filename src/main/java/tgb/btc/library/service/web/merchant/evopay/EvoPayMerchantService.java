package tgb.btc.library.service.web.merchant.evopay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.vo.web.merchant.evopay.CreateOrderRequest;
import tgb.btc.library.vo.web.merchant.evopay.ListOrderRequest;
import tgb.btc.library.vo.web.merchant.evopay.ListOrderResponse;
import tgb.btc.library.vo.web.merchant.evopay.OrderResponse;

import java.util.List;
import java.util.Objects;

@Service
public class EvoPayMerchantService implements IMerchantService {

    private final String botName;

    private final RestTemplate restTemplate;

    private final String createOrderUrl;

    private final String listOrderUrl;

    private final HttpHeaders httpHeaders;

    public EvoPayMerchantService(@Value("${evoPay.api.key:null}") String apiKey,
                                 @Value("${evoPay.api.url:null}") String baseUrl,
                                 @Value("${bot.name}") String botName, RestTemplate restTemplate) {
        this.botName = botName;
        this.restTemplate = restTemplate;
        this.createOrderUrl = baseUrl + "/v1/api/order/payin";
        this.listOrderUrl = baseUrl + "/v1/api/order/list";
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("x-api-key", apiKey);
        this.httpHeaders = httpHeaders;
    }

    public OrderResponse createOrder(Deal deal) {
        CreateOrderRequest request = new CreateOrderRequest();
        request.setCustomId(botName + deal.getPid());
        request.setFiatSum(deal.getAmount().intValue());
        request.setPaymentMethod(deal.getPaymentType().getEvoPayPaymentMethod());
        HttpEntity<CreateOrderRequest> httpEntity = new HttpEntity<>(request, httpHeaders);
        ResponseEntity<OrderResponse> response = restTemplate.exchange(createOrderUrl, HttpMethod.POST, httpEntity, OrderResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении реквизитов пустое.");
        }
        return response.getBody();
    }

    public OrderResponse getOrder(String id) {
        ListOrderRequest listOrderRequest = new ListOrderRequest();
        listOrderRequest.setOrderId(id);
        HttpEntity<ListOrderRequest> httpEntity = new HttpEntity<>(listOrderRequest, httpHeaders);
        ResponseEntity<ListOrderResponse> response = restTemplate.exchange(listOrderUrl, HttpMethod.GET, httpEntity, ListOrderResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении реквизитов пустое.");
        }
        return response.getBody().getEntries().get(0);
    }

    public List<OrderResponse> getOrders(List<EvoPayStatus> statuses) {
        ListOrderRequest listOrderRequest = new ListOrderRequest();
        listOrderRequest.setPage(1);
        listOrderRequest.setLimit(50);
        listOrderRequest.setStatuses(statuses);
        HttpEntity<ListOrderRequest> httpEntity = new HttpEntity<>(listOrderRequest, httpHeaders);
        ResponseEntity<ListOrderResponse> response = restTemplate.exchange(listOrderUrl, HttpMethod.GET, httpEntity, ListOrderResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении реквизитов пустое.");
        }
        return response.getBody().getEntries();
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.EVO_PAY;
    }
}
