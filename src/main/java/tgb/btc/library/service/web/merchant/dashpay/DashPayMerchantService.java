package tgb.btc.library.service.web.merchant.dashpay;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderMethod;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderType;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.dashpay.CreateOrderRequest;
import tgb.btc.library.vo.web.merchant.dashpay.OrderResponse;
import tgb.btc.library.vo.web.merchant.dashpay.OrdersRequest;
import tgb.btc.library.vo.web.merchant.dashpay.OrdersResponse;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class DashPayMerchantService implements IMerchantService {

    private final RestTemplate restTemplate;

    private final String apiKey;

    private final String basicToken;

    private final String botName;

    private final String createOrderUrl;

    private final String cancelOrderUrl;

    private final String ordersUrl;

    public DashPayMerchantService(RestTemplate restTemplate, @Value("${dashpay.api.key:null}") String apiKey,
                                  @Value("${dashpay.api.basic.token:null}") String basicToken,
                                  @Value("${bot.name:null}") String botName, @Value("${dashpay.api.url.client:null}") String clientUrl,
                                  @Value("${dashpay.api.url.main:null}") String mainUrl) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.basicToken = basicToken;
        this.botName = botName;
        this.createOrderUrl = clientUrl + "/payments";
        this.cancelOrderUrl = mainUrl + "/orders/%s/edit";
        this.ordersUrl = mainUrl + "/orders";
    }

    public OrderResponse createOrder(Deal deal) {
        log.debug("Запрос на создание ордера для сделки №{}", deal.getPid());
        CreateOrderRequest request = CreateOrderRequest.builder()
                .id(botName + deal.getPid())
                .type(OrderType.DEPOSIT)
                .method(OrderMethod.CARD_NUMBER)
                .sum(deal.getAmount().doubleValue())
                .customer(CreateOrderRequest.Customer.builder().id(deal.getUser().getChatId().toString()).build())
                .build();

        HttpHeaders headers = getHeaders(false);
        ObjectNode body = JacksonUtil.DEFAULT_OBJECT_MAPPER.createObjectNode();
        body.set("order", JacksonUtil.DEFAULT_OBJECT_MAPPER.valueToTree(request));
        HttpEntity<ObjectNode> entity = new HttpEntity<>(body, headers);
        ResponseEntity<OrderResponse> response = restTemplate.exchange(
                createOrderUrl,
                HttpMethod.POST,
                entity,
                OrderResponse.class
        );
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании ордера пустое.");
        }
        return response.getBody();
    }

    public void cancelOrder(String dashPayOrderId) {
        String body = "{\"order\":{\"status\":{\"code\":\"canceled\"}}}";
        HttpEntity<String> entity = new HttpEntity<>(body, getHeaders(true));
        ResponseEntity<Object> response = restTemplate.exchange(
                cancelOrderUrl.formatted(dashPayOrderId),
                HttpMethod.PUT,
                entity,
                Object.class
        );
    }

    private HttpHeaders getHeaders(boolean isBearer) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", isBearer ? apiKey : basicToken);
        headers.add("Idempotence-Key", String.valueOf(System.currentTimeMillis()));
        headers.add("Content-Type", "application/json");
        return headers;
    }

    public OrdersResponse getOrders(LocalDateTime from, LocalDateTime to) {
        OrdersRequest ordersRequest = OrdersRequest.builder()
                .perPage(100)
                .currentPage(1)
                .createdAtFrom(from)
                .createdAtTo(to)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", apiKey);
        headers.add("Idempotence-Key", String.valueOf(System.currentTimeMillis()));
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<OrdersResponse> response = restTemplate.exchange(
                ordersUrl + ordersRequest.buildParams(),
                HttpMethod.GET,
                entity,
                OrdersResponse.class
        );
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении списка ордеров пустое.");
        }
        return response.getBody();
    }



    public Merchant getMerchant() {
        return Merchant.DASH_PAY;
    }
}
