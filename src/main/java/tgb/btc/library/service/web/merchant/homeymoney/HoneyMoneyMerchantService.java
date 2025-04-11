package tgb.btc.library.service.web.merchant.homeymoney;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import tgb.btc.api.web.INotifier;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyMethod;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.repository.bot.deal.ModifyDealRepository;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.util.web.JacksonUtil;
import tgb.btc.library.vo.web.merchant.honeymoney.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
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

    private final IReadDealService readDealService;

    private final ModifyDealRepository modifyDealRepository;

    private final INotifier notifier;

    public HoneyMoneyMerchantService(RestTemplate restTemplate,
                                     @Value("${main.url:null}") String mainUrl,
                                     @Value("${honeymoney.api.base.url:null}") String baseUrl,
                                     @Value("${honeymoney.api.secret:null}") String secret,
                                     @Value("${honeymoney.api.token.request.url:null}") String tokenRequestUrl,
                                     @Value("${honeymoney.api.clientid:null}") String clientId,
                                     @Value("${bot.name}") String botName,
                                     @Value("${honeymoney.api.signKey:null}") String signKey,
                                     @Value("${honeymoney.api.token:null}") String apiToken,
                                     IReadDealService readDealService, ModifyDealRepository modifyDealRepository,
                                     INotifier notifier) {
        this.restTemplate = restTemplate;
        this.tokenRequestUrl = tokenRequestUrl;
        this.readDealService = readDealService;
        this.modifyDealRepository = modifyDealRepository;
        this.notifier = notifier;
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

    public CreateOrderResponse createRequest(Deal deal) throws JsonProcessingException, URISyntaxException {
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
        String signature = generateSignature(body, new URI(baseUrl + requestPath));
        HttpHeaders httpHeaders = new HttpHeaders();

        if (Objects.isNull(tokenResponse)) {
            updateToken();
        }
        httpHeaders.add("Authorization", tokenResponse.getTokenType() + " " + tokenResponse.getAccessToken());
        httpHeaders.add("X-Signature", signature);
        httpHeaders.add("Content-Type", "application/json");
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

    public String generateSignature(String requestJson, URI url) {
        String signatureString = requestJson + url.getPath() +
                (url.getQuery() != null ? url.getQuery() : "");

        HmacUtils hmacUtils = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, signKey.getBytes(StandardCharsets.UTF_8));
        byte[] hmacSha256 = hmacUtils.hmac(signatureString.getBytes(StandardCharsets.UTF_8));
        return Hex.encodeHexString(hmacSha256);
    }

    public void updateStatus(TransactionCallback transactionCallback) {
        Deal deal = readDealService.getByMerchantOrderId(transactionCallback.getOrderId());
        if (Objects.nonNull(deal)) {
            deal.setMerchantOrderStatus(transactionCallback.getStatus().name());
            modifyDealRepository.save(deal);
            notifier.merchantUpdateStatus(deal.getPid(), "HoneyMoney обновил статус по сделке №" + deal.getPid()
                    + " до \"" + transactionCallback.getStatus().getDescription() + "\".");
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
