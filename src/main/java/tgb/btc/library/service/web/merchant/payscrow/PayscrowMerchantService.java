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
import tgb.btc.library.constants.enums.web.merchant.payscrow.BankCard;
import tgb.btc.library.constants.enums.web.merchant.payscrow.CurrencyType;
import tgb.btc.library.constants.enums.web.merchant.payscrow.FeeType;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderSide;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.payscrow.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class PayscrowMerchantService {

    private final Map<String, String> PAYMENT_METHODS_IDS = Map.of(
            "Альфа-Банк", "4f591bcc-29f8-4598-9828-5b109a25b509",
            "Сбербанк", "af3daf65-b6b6-450b-b28d-54b97436ef4a",
            "Тинькофф", "cd797fcb-5b5a-47a3-8b54-35f5f7000344",
            "Любой банк РФ", "b11d98ad-1e09-4e63-8c4a-f1d8a4b9ce3f",

            "СБП Тинькофф", "33c6fe18-641d-41f5-a3fc-db975c63fe6f",
            "СБП Альфа-Банк", "8880d5b5-2c82-4cef-8f94-b7d5d2cbefe1",
            "СБП Сбербанк", "a6636989-0bd9-4cf1-8ec3-83c07b08f25f",
            "СБП", "894387d7-b8b6-4dab-82ee-dd1106f7369e"
    );

    @Value("${payscrow.api.key}")
    private String apiKey;

    @Value("${payscrow.api.secret}")
    private String apiSecret;

    private final String domain;

    private final RestTemplate restTemplate;
    
    private final String relativePaymentMethodsUrl = "/api/v1/Misc/ListPaymentMethods";

    private final String relativeCreateOrderUrl = "/api/v1/Orders/Create";

    private final String paymentMethodsUrl;

    private final String createOrderUrl;

    public PayscrowMerchantService(RestTemplate restTemplate, @Value("${payscrow.api.domain}") String domain) {
        this.restTemplate = restTemplate;
        this.domain = domain;
        paymentMethodsUrl = domain + relativePaymentMethodsUrl;
        createOrderUrl = domain + relativeCreateOrderUrl;
    }

    public String getPaymentMethodName(String methodId) {
        for (Map.Entry<String, String> entry : PAYMENT_METHODS_IDS.entrySet()) {
            if (methodId.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public Map<String, String> getPaymentMethodsIds() {
        return PAYMENT_METHODS_IDS;
    }

    public List<PaymentMethod> getPaymentMethods() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-API-Key", this.apiKey);
        PaymentMethodsFilter paymentMethodsFilter = new PaymentMethodsFilter();
        paymentMethodsFilter.setAvailableOnly(true);
        paymentMethodsFilter.setType(BankCard.BANK_CARD);
        paymentMethodsFilter.setFiatName("RUB");
        String body;
        try {
            body = JacksonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(paymentMethodsFilter);
        } catch (JsonProcessingException e) {
            throw new BaseException("Ошибка при парсинге значений фильтра в тело.");
        }
        headers.add("X-API-Sign", getSign(relativePaymentMethodsUrl, body));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<ListPaymentMethodsResponse> response = restTemplate.exchange(
                paymentMethodsUrl,
                HttpMethod.POST,
                entity,
                ListPaymentMethodsResponse.class
        );
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении списка методов оплаты пустое.");
        }
        return response.getBody().getPaymentMethods();
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
                .externalOrderId(deal.getPid().toString())
                .orderSide(OrderSide.BUY)
                .basePaymentMethodId(deal.getPaymentType().getPayscrowPaymentMethodId())
                .targetAmount(deal.getAmount())
                .feeType(FeeType.CHARGE_MERCHANT)
                .currencyType(CurrencyType.FIAT)
                .currency(deal.getFiatCurrency().name())
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("X-API-Key", this.apiKey);
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
}
