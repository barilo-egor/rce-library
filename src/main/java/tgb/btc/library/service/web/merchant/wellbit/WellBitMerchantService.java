package tgb.btc.library.service.web.merchant.wellbit;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.wellbit.WellBitMethod;
import tgb.btc.library.constants.enums.web.merchant.wellbit.WellBitStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.wellbit.CreateOrderRequest;
import tgb.btc.library.vo.web.merchant.wellbit.Order;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
public class WellBitMerchantService implements IMerchantService {

    private final RestTemplate restTemplate;

    private final String botName;

    private final String secret;

    private final String token;

    private final String createOrderUrl;

    private final String getOrderUrl;

    private final String setPayUrl;

    public WellBitMerchantService(RestTemplate restTemplate,
                                  @Value("${wellBit.api.secret}") String apiSecret,
                                  @Value("${wellBit.api.login}") String login,
                                  @Value("${wellBit.api.id}") String id,
                                  @Value("${wellBit.api.token}") String token,
                                  @Value("${wellBit.api.url}") String url,
                                  @Value("${bot.name}") String botName) throws NoSuchAlgorithmException {
        this.restTemplate = restTemplate;
        this.botName = botName;
        MessageDigest md = MessageDigest.getInstance("MD5");
        String combined = apiSecret + login + id;
        byte[] messageDigest = md.digest(combined.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        StringBuilder hashtext = new StringBuilder(no.toString(16));
        while (hashtext.length() < 32) {
            hashtext.insert(0, "0");
        }
        this.secret = hashtext.toString();
        this.token = token;
        this.createOrderUrl = url + "/api/payment/make";
        this.getOrderUrl = url + "/api/payment/get";
        this.setPayUrl = url + "/api/payment/setpay";
    }

    public Order createOrder(Deal deal) throws JsonProcessingException {
        WellBitMethod wellBitMethod = deal.getPaymentType().getWellBitMethod();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setCredentialRequire("yes");
        createOrderRequest.setAmount(deal.getAmount().intValue());
        createOrderRequest.setCredentialType(wellBitMethod.getValue());
        createOrderRequest.setCustomNumber(botName + deal.getPid() + "_" + System.currentTimeMillis());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        httpHeaders.add("secret", secret);
        HttpEntity<CreateOrderRequest> httpEntity = new HttpEntity<>(createOrderRequest, httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                createOrderUrl,
                HttpMethod.POST,
                httpEntity,
                String.class
        );
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании транзакции пустое.");
        }
        List<Order> orders = JacksonUtil.DEFAULT_OBJECT_MAPPER.readValue(response.getBody(),
                JacksonUtil.DEFAULT_OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, Order.class));
        return orders.get(0);
    }

    public WellBitStatus getStatus(String id) {
        ParameterizedTypeReference<List<Order>> responseType = new ParameterizedTypeReference<>() {};
        ResponseEntity<List<Order>> response = restTemplate.exchange(
                getOrderUrl,
                HttpMethod.POST,
                getIdBodyHttpEntity(id),
                responseType
        );
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании транзакции пустое.");
        }
        if (response.getBody().isEmpty()) {
            return null;
        }
        return response.getBody().get(0).getPayment().getStatus();
    }

    public void setPay(String id) {
        restTemplate.exchange(
                setPayUrl,
                HttpMethod.POST,
                getIdBodyHttpEntity(id),
                String.class
        );
    }

    private HttpEntity<IdBody> getIdBodyHttpEntity(String id) {
        IdBody idBody = new IdBody();
        idBody.setId(Long.parseLong(id));
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        httpHeaders.add("secret", secret);
        return new HttpEntity<>(idBody, httpHeaders);
    }

    @Data
    public static class IdBody {
        private Long id;
    }

    @Override
    public void cancelOrder(String orderId) {
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.WELL_BIT;
    }
}
