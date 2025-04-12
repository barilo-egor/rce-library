package tgb.btc.library.service.web.merchant.onlypays;

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
import tgb.btc.library.interfaces.util.IBigDecimalService;
import tgb.btc.library.service.web.merchant.IMerchantService;
import tgb.btc.library.vo.web.merchant.onlypays.GetRequisiteRequest;
import tgb.btc.library.vo.web.merchant.onlypays.GetRequisiteResponse;
import tgb.btc.library.vo.web.merchant.onlypays.GetStatusRequest;
import tgb.btc.library.vo.web.merchant.onlypays.GetStatusResponse;

import java.util.Objects;

@Service
public class OnlyPaysMerchantService implements IMerchantService {

    private final String getRequisiteUrl;

    private final String getStatusUrl;

    private final String apiSecret;

    private final String apiId;

    private final String botName;

    private final RestTemplate restTemplate;

    private final IBigDecimalService bigDecimalService;

    public OnlyPaysMerchantService(@Value("${onlyPays.api.url:null}") String baseUrl,
                                   @Value("${onlyPays.api.secret:null}") String apiSecret,
                                   @Value("${onlyPays.api.id:null}") String apiId,
                                   @Value("${bot.name}") String botName,
                                   RestTemplate restTemplate, IBigDecimalService bigDecimalService) {
        this.getRequisiteUrl = baseUrl + "/get_requisite";
        this.getStatusUrl = baseUrl + "/get_status";
        this.restTemplate = restTemplate;
        this.apiSecret = apiSecret;
        this.apiId = apiId;
        this.bigDecimalService = bigDecimalService;
        this.botName = botName;
    }

    public GetRequisiteResponse requisiteRequest(Deal deal) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        GetRequisiteRequest getRequisiteRequest = new GetRequisiteRequest();
        getRequisiteRequest.setApiId(apiId);
        getRequisiteRequest.setAmount(Integer.parseInt(bigDecimalService.roundToPlainString(deal.getAmount(), 0)));
        getRequisiteRequest.setPaymentType(deal.getPaymentType().getOnlyPaysPaymentType());
        getRequisiteRequest.setSecretKey(apiSecret);
        getRequisiteRequest.setPersonalId(botName + deal.getPid());
        HttpEntity<GetRequisiteRequest> httpEntity = new HttpEntity<>(getRequisiteRequest, httpHeaders);
        ResponseEntity<GetRequisiteResponse> response = restTemplate.exchange(getRequisiteUrl, HttpMethod.POST, httpEntity, GetRequisiteResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении реквизитов пустое.");
        }
        return response.getBody();
    }

    public GetStatusResponse statusRequest(String id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        GetStatusRequest getStatusRequest = new GetStatusRequest();
        getStatusRequest.setApiId(apiId);
        getStatusRequest.setSecretKey(apiSecret);
        getStatusRequest.setId(id);
        HttpEntity<GetStatusRequest> httpEntity = new HttpEntity<>(getStatusRequest, httpHeaders);
        ResponseEntity<GetStatusResponse> response = restTemplate.exchange(getStatusUrl, HttpMethod.POST, httpEntity, GetStatusResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при получении статуса пустое.");
        }
        return response.getBody();
    }

    @Override
    public void cancelOrder(String orderId) {
        // no implementation for this merchant
    }

    @Override
    public Merchant getMerchant() {
        return Merchant.ONLY_PAYS;
    }
}
