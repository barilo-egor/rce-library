package tgb.btc.library.service.web.merchant.homeymoney;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyMethod;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.vo.web.merchant.honeymoney.CreateCardOrderRequest;
import tgb.btc.library.vo.web.merchant.honeymoney.CreateCardOrderResponse;
import tgb.btc.library.vo.web.merchant.honeymoney.TokenRequest;
import tgb.btc.library.vo.web.merchant.honeymoney.TokenResponse;

import java.util.Objects;

public class HoneyMoneyMerchantService implements IMerchantService {

    private final RestTemplate restTemplate;

    private final String tokenRequestUrl;

    private final HttpEntity<MultiValueMap<String,String>> tokenRequestHttpEntity;

    private final String cardOrderUrl;

    private final String botName;

    private TokenResponse tokenResponse;

    public HoneyMoneyMerchantService(RestTemplate restTemplate,
                                     @Value("${honeymoney.api.base.url:null}") String baseUrl,
                                     @Value("${honeymoney.api.secret:null}") String secret,
                                     @Value("${honeymoney.api.token.request.url:null}") String tokenRequestUrl,
                                     @Value("${honeymoney.api.clientid:null}") String clientId,
                                     @Value("${bot.name}") String botName) {
        this.restTemplate = restTemplate;
        this.tokenRequestUrl = tokenRequestUrl;
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setClientId(clientId);
        tokenRequest.setClientSecret(secret);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        this.tokenRequestHttpEntity = new HttpEntity<>(tokenRequest.toFormData(), httpHeaders);
        this.cardOrderUrl = baseUrl + "/v2/merchant/transactions";
        this.botName = botName;
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

    public void createOrder(Deal deal) {
        HoneyMoneyMethod honeyMoneyMethod = deal.getPaymentType().getHoneyMoneyMethod();
        switch (honeyMoneyMethod) {
            case CARD -> createRequest(
                    botName + deal.getPid(),
                    deal.getAmount().intValue(),
                    deal.getUser().getChatId().toString(),
                    honeyMoneyMethod.getBank()
            );
        }
    }

    public CreateCardOrderResponse createRequest(String extId, Integer amount, String clientId, String bank) {
        CreateCardOrderRequest createCardOrderRequest = new CreateCardOrderRequest();
        createCardOrderRequest.setAmount(amount);
        CreateCardOrderRequest.ClientDetails clientDetails = new CreateCardOrderRequest.ClientDetails();
        clientDetails.setClientId(clientId);
        createCardOrderRequest.setClientDetails(clientDetails);
        createCardOrderRequest.setExtId(extId);
        createCardOrderRequest.setBank(bank);
        return null;
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.HONEY_MONEY;
    }
}
