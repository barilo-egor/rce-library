package tgb.btc.library.service.web.merchant.crocopay;

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
import tgb.btc.library.vo.web.merchant.crocopay.CreateInvoiceRequest;
import tgb.btc.library.vo.web.merchant.crocopay.CreateInvoiceResponse;
import tgb.btc.library.vo.web.merchant.crocopay.GetInvoiceResponse;

import java.util.Objects;

@Service
public class CrocoPayMerchantService implements IMerchantService {

    private final RestTemplate restTemplate;

    private final String clientId;

    private final String clientSecret;

    private final String invoicesUrl;

    public CrocoPayMerchantService(RestTemplate restTemplate,
                                   @Value("${crocoPay.api.clientId}") String clientId,
                                   @Value("${crocoPay.api.clientSecret}") String clientSecret,
                                   @Value("${crocoPay.api.url}") String url) {
        this.restTemplate = restTemplate;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.invoicesUrl = url + "/api/v2/h2h/invoices";
    }


    public CreateInvoiceResponse createInvoice(Deal deal) {
        CreateInvoiceRequest request = new CreateInvoiceRequest();
        request.setAmount(deal.getAmount().intValue());
        request.setCrocoPayMethod(deal.getPaymentType().getCrocoPayMethod());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Client-Id", clientId);
        headers.add("Client-Secret", clientSecret);
        HttpEntity<CreateInvoiceRequest> requestEntity = new HttpEntity<>(request, headers);
        ResponseEntity<CreateInvoiceResponse> response = restTemplate.exchange(invoicesUrl, HttpMethod.POST, requestEntity, CreateInvoiceResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании ордера пустое.");
        }
        return response.getBody();
    }

    public GetInvoiceResponse getInvoice(String id) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Client-Id", clientId);
        headers.add("Client-Secret", clientSecret);
        HttpEntity<CreateInvoiceRequest> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<GetInvoiceResponse> response =
                restTemplate.exchange(invoicesUrl + "/" + id, HttpMethod.GET, requestEntity, GetInvoiceResponse.class);
        if (Objects.isNull(response.getBody())) {
            throw new BaseException("Тело ответа при создании ордера пустое.");
        }
        return response.getBody();
    }

    @Override
    public void cancelOrder(String orderId) {

    }

    @Override
    public Merchant getMerchant() {
        return Merchant.CROCO_PAY;
    }
}
