package tgb.btc.library.service.web.merchant.payscrow;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.payscrow.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Service
@Slf4j
public class PayscrowMerchantService implements IMerchantService {

    private final String apiKey;

    private final String apiSecret;

    private final String botName;

    private final RestTemplate restTemplate;

    private final String relativeCreateOrderUrl = "/api/v1/Orders/Create";

    private final String relativeCancelOrderUrl = "/api/v1/Orders/Cancel";

    private final String relativeListOrderUrl = "/api/v1/Orders/List";

    private final String cancelOrderUrl;

    private final String createOrderUrl;

    private final String listOrderUrl;

    public PayscrowMerchantService(RestTemplate restTemplate,
                                   @Value("${payscrow.api.domain}") String domain,
                                   @Value("${payscrow.api.key}") String apiKey,
                                   @Value("${payscrow.api.secret}") String apiSecret,
                                   @Value("${bot.name}") String botName) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.botName = botName;
        this.restTemplate = restTemplate;
        this.createOrderUrl = domain + relativeCreateOrderUrl;
        this.cancelOrderUrl = domain + relativeCancelOrderUrl;
        this.listOrderUrl = domain + relativeListOrderUrl;
    }

    public String getSign(String url, String body) {
        try {
            // Создаем экземпляр MessageDigest для SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Формируем данные для хэширования
            String data = url + "/" + apiSecret + "/" + apiKey;
            if (Objects.nonNull(body) && !body.isBlank()) {
                data += "\r\n" + body;
            }

            // Хэшируем данные
            byte[] hashBytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));

            // Преобразуем байты в hex-строку
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("Ошибка при формировании подписи для Payscrow запроса.", e);
            throw new BaseException(e);
        }
    }

    public PayscrowOrderResponse createBuyOrder(Deal deal) {
        PayscrowOrderRequest request = PayscrowOrderRequest.builder()
                .externalOrderId(botName + deal.getPid().toString())
                .basePaymentMethodId(deal.getPaymentType().getPayscrowPaymentMethodId())
                .targetAmount(deal.getAmount())
                .currency(deal.getFiatCurrency().name())
                .build();
        HttpHeaders headers = getDefaultHeaders();
        String body;
        try {
            body = JacksonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            throw new BaseException("Ошибка при парсинге значений фильтра в тело.");
        }
        headers.add("X-API-Sign", getSign(relativeCreateOrderUrl, body));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<PayscrowOrderResponse> response = restTemplate.exchange(
                createOrderUrl,
                HttpMethod.POST,
                entity,
                PayscrowOrderResponse.class
        );
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении списка методов оплаты пустое.");
        }
        return response.getBody();
    }

    public void cancelOrder(String orderId) {
        log.debug("Запрос на отмену ордера orderId={}", orderId);
        HttpHeaders headers = getDefaultHeaders();
        String body;
        try {
            body = JacksonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(
                    PayscrowCancelOrderRequest.builder()
                            .orderId(Integer.parseInt(orderId))
                            .requestedByCustomer(true)
                            .build());
        } catch (JsonProcessingException e) {
            throw new BaseException("Ошибка при парсинге значений фильтра в тело.");
        }
        headers.add("X-API-Sign", getSign(relativeCancelOrderUrl, body));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        restTemplate.exchange(
                cancelOrderUrl,
                HttpMethod.POST,
                entity,
                PayscrowResponse.class
        );
    }

    public ListOrdersResponse getLast30MinutesOrders() {
        LocalDateTime now = OffsetDateTime.now(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime from = now.minusMinutes(30);
        ListOrdersRequest listOrdersRequest = ListOrdersRequest.builder()
                .from(from)
                .to(now)
                .build();
        HttpHeaders headers = getDefaultHeaders();
        String body;
        try {
            body = JacksonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(listOrdersRequest);
        } catch (JsonProcessingException e) {
            throw new BaseException("Ошибка при парсинге значений фильтра в тело.");
        }
        headers.add("X-API-Sign", getSign(relativeListOrderUrl, body));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<ListOrdersResponse> response = restTemplate.exchange(
                listOrderUrl,
                HttpMethod.POST,
                entity,
                ListOrdersResponse.class
        );
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении списка методов оплаты пустое.");
        }
        return response.getBody();
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-API-Key", this.apiKey);
        return headers;
    }

    public Merchant getMerchant() {
        return Merchant.PAYSCROW;
    }
}
