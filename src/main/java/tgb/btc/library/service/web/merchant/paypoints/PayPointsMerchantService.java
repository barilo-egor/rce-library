package tgb.btc.library.service.web.merchant.paypoints;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.Merchant;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.util.IBigDecimalService;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.vo.web.merchant.paypoints.CardTransactionResponse;
import tgb.btc.library.vo.web.merchant.paypoints.RequestBody;
import tgb.btc.library.vo.web.merchant.paypoints.SbpTransactionResponse;
import tgb.btc.library.vo.web.merchant.paypoints.TransactionResponse;

import java.util.Objects;


@Service
public class PayPointsMerchantService implements IMerchantService {

    private final String baseUrl;

    private final String cardUrl;

    private final String sbpUrl;

    private final String transgranSbp;

    private final String token;

    private final String botName;

    private final RestTemplate restTemplate;

    private final IBigDecimalService bigDecimalService;

    public PayPointsMerchantService(@Value("${paypoints.api.baseUrl:null}") String baseUrl,
                                    @Value("${paypoints.api.token:null}") String token, RestTemplate restTemplate,
                                    @Value("${bot.name}") String botName,
                                    IBigDecimalService bigDecimalService) {
        this.baseUrl = baseUrl;
        this.cardUrl = baseUrl + "/transactions/card";
        this.sbpUrl = baseUrl + "/transactions/sbp";
        this.transgranSbp = baseUrl + "/transactions/transgran-sbp";
        this.botName = botName;
        this.token = token;
        this.restTemplate = restTemplate;
        this.bigDecimalService = bigDecimalService;
    }

    public CardTransactionResponse createCardTransaction(Deal deal) {
        HttpEntity<RequestBody> httpEntity = createTransaction(deal);
        ResponseEntity<CardTransactionResponse> response = restTemplate.exchange(cardUrl, HttpMethod.POST, httpEntity, CardTransactionResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании транзакции пустое.");
        }
        return response.getBody();
    }

    public SbpTransactionResponse createSbpTransaction(Deal deal) {
        return createSbpTransaction(deal, sbpUrl);
    }

    public SbpTransactionResponse createTransgranSbpTransaction(Deal deal) {
        return createSbpTransaction(deal, transgranSbp);
    }

    private SbpTransactionResponse createSbpTransaction(Deal deal, String url) {
        HttpEntity<RequestBody> httpEntity = createTransaction(deal);
        ResponseEntity<SbpTransactionResponse> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, SbpTransactionResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании транзакции пустое.");
        }
        return response.getBody();
    }

    private HttpEntity<RequestBody> createTransaction(Deal deal) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);
        RequestBody requestBody = new RequestBody();
        requestBody.setAmount(Integer.parseInt(bigDecimalService.roundToPlainString(deal.getAmount(), 0)));
        requestBody.setCurrency(FiatCurrency.RUB);
        requestBody.setMerchantTransactionId(botName + deal.getPid());
        return new HttpEntity<>(requestBody, httpHeaders);
    }

    public PayPointsStatus getStatus(Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(httpHeaders);
        ResponseEntity<TransactionResponse> response = restTemplate.exchange(baseUrl + "/transactions/" + id, HttpMethod.GET, httpEntity, TransactionResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании получении статуса транзакции пустое.");
        }
        return response.getBody().getStatus();
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.PAY_POINTS;
    }
}
