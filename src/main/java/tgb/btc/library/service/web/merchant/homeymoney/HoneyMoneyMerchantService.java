package tgb.btc.library.service.web.merchant.homeymoney;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyMethod;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.honeymoney.CreateOrderRequest;
import tgb.btc.library.vo.web.merchant.honeymoney.CreateOrderResponse;
import tgb.btc.library.vo.web.merchant.honeymoney.TokenRequest;
import tgb.btc.library.vo.web.merchant.honeymoney.TokenResponse;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HoneyMoneyMerchantService implements IMerchantService {

    private final RestTemplate restTemplate;

    private final String tokenRequestUrl;

    private final HttpEntity<MultiValueMap<String,String>> tokenRequestHttpEntity;

    private final String baseUrl;

    private final Map<HoneyMoneyMethod, String> requestsPaths;

    private final String botName;

    private TokenResponse tokenResponse;

    private final String signKey;

    private final String apiToken;

    private final String callbackUrl;

    public HoneyMoneyMerchantService(RestTemplate restTemplate,
                                     @Value("${main.url:null}") String mainUrl,
                                     @Value("${honeymoney.api.base.url:null}") String baseUrl,
                                     @Value("${honeymoney.api.secret:null}") String secret,
                                     @Value("${honeymoney.api.token.request.url:null}") String tokenRequestUrl,
                                     @Value("${honeymoney.api.clientid:null}") String clientId,
                                     @Value("${bot.name}") String botName,
                                     @Value("${honeymoney.api.signKey}") String signKey,
                                     @Value("${honeymoney.api.token}") String apiToken) {
        this.restTemplate = restTemplate;
        this.tokenRequestUrl = tokenRequestUrl;
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientId(clientId);
        tokenRequest.setClientSecret(secret);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        this.tokenRequestHttpEntity = new HttpEntity<>(tokenRequest.toFormData(), httpHeaders);
        requestsPaths = new HashMap<>();
        requestsPaths.put(HoneyMoneyMethod.CARD, "/v2/merchant/transactions");
        requestsPaths.put(HoneyMoneyMethod.SBP, "/v2/merchant/transactions/sbp");
        requestsPaths.put(HoneyMoneyMethod.CROSS_BORDER, "/v2/merchant/transactions/cross-border");
        this.botName = botName;
        this.signKey = signKey;
        this.apiToken = apiToken;
        this.baseUrl = baseUrl;
        this.callbackUrl = mainUrl + "/merchant/honeymoney";
    }

    public void updateToken() {
        ResponseEntity<TokenResponse> tokenResponseResponseEntity =
                restTemplate.exchange(tokenRequestUrl, HttpMethod.POST, tokenRequestHttpEntity, TokenResponse.class);
        if (Objects.nonNull(tokenResponseResponseEntity.getBody())) {
            tokenResponse = tokenResponseResponseEntity.getBody();
        } else {
            throw new BaseException("Ответ при получении токена содержит пустое тело.");
        }
    }

    public CreateOrderResponse createRequest(Deal deal) throws JsonProcessingException {
        HoneyMoneyMethod honeyMoneyMethod = deal.getPaymentType().getHoneyMoneyMethod();
        CreateOrderRequest createOrderRequest = new CreateOrderRequest();
        createOrderRequest.setAmount(deal.getAmount().intValue());
        CreateOrderRequest.ClientDetails clientDetails = new CreateOrderRequest.ClientDetails();
        clientDetails.setClientId(deal.getUser().getChatId().toString());
        createOrderRequest.setClientDetails(clientDetails);
        createOrderRequest.setExtId(botName + deal.getPid());
        createOrderRequest.setBank(honeyMoneyMethod.getBank());
        createOrderRequest.setCallbackUrl(callbackUrl);
        String body = JacksonUtil.DEFAULT_OBJECT_MAPPER.writeValueAsString(createOrderRequest);
        String requestPath = requestsPaths.get(honeyMoneyMethod);
        String signature = generateSignature(body, requestPath);
        HttpHeaders httpHeaders = new HttpHeaders();

        if (Objects.isNull(tokenResponse)) {
            updateToken();
        }
        httpHeaders.add("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());
        httpHeaders.add("X-Signature", signature);
        HttpEntity<String> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<CreateOrderResponse> response;
        try {
            response = restTemplate.exchange(baseUrl + requestPath, HttpMethod.POST, httpEntity, CreateOrderResponse.class);
        } catch (HttpClientErrorException.Unauthorized unauthorized) {
            updateToken();
            response = restTemplate.exchange(baseUrl + requestPath, HttpMethod.POST, httpEntity, CreateOrderResponse.class);
        }
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании ордера пустое.");
        }
        return response.getBody();
    }

    public String generateSignature(String requestBody, String requestPath) {
        try {
            String dataToSign = requestBody + requestPath;
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(signKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] hmacBytes = mac.doFinal(dataToSign.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("HMAC SHA-256 algorithm not available", e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key for HMAC SHA-256", e);
        }
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.HONEY_MONEY;
    }
}
