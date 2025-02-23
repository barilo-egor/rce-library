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
import tgb.btc.library.constants.enums.web.merchant.payscrow.BankCard;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.payscrow.ListPaymentMethodsResponse;
import tgb.btc.library.vo.web.merchant.payscrow.PaymentMethod;
import tgb.btc.library.vo.web.merchant.payscrow.PaymentMethodsFilter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class PayscrowMerchantService {

    @Value("${payscrow.api.key}")
    private String apiKey;

    @Value("${payscrow.api.secret}")
    private String apiSecret;

    private final String domain;

    private final RestTemplate restTemplate;
    
    private final String relativePaymentMethodsUrl = "/api/v1/Misc/ListPaymentMethods";

    private final String paymentMethodsUrl;

    public PayscrowMerchantService(RestTemplate restTemplate, @Value("${payscrow.api.domain}") String domain) {
        this.restTemplate = restTemplate;
        this.domain = domain;
        paymentMethodsUrl = domain + relativePaymentMethodsUrl;
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

}
