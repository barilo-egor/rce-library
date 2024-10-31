package tgb.btc.library.service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.web.ICryptoWithdrawalService;
import tgb.btc.library.interfaces.web.IRequestService;
import tgb.btc.library.vo.web.ApiResponse;
import tgb.btc.library.vo.web.RequestHeader;
import tgb.btc.library.vo.web.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CryptoWithdrawalService implements ICryptoWithdrawalService {

    private final String authenticateUrl;

    private final String balanceUrl;

    private final List<RequestParam> authenticateParams;

    private final RequestHeader requestAuthorizationHeader;

    private final IRequestService requestService;

    @Autowired
    public CryptoWithdrawalService(IRequestService requestService,
                                   @Value("${crypto-withdrawal.url}") String cryptoWithdrawalUrl,
                                   @Value("${crypto-withdrawal.username}") String username,
                                   @Value("${crypto-withdrawal.password}") String password) {
        this.requestService = requestService;
        authenticateUrl = cryptoWithdrawalUrl + "/authenticate";
        balanceUrl = cryptoWithdrawalUrl + "/balance";
        authenticateParams = new ArrayList<>();
        authenticateParams.add(RequestParam.builder().key("username").value(username).build());
        authenticateParams.add(RequestParam.builder().key("password").value(password).build());
        requestAuthorizationHeader = new RequestHeader();
        requestAuthorizationHeader.setName("Authorization");
        log.debug("Сервис для взаимодействия с микросервисом crypto-withdrawal успешно загружен в контекст. Url = {}", cryptoWithdrawalUrl);
    }

    private void authenticate() {
        try {
            ResponseEntity<String> response = requestService.post(authenticateUrl, authenticateParams, String.class);
            requestAuthorizationHeader.setValue("Bearer " + response.getBody());
        } catch (Exception e) {
            log.error("Ошибка аутентификации в микросервисе crypto-withdrawal: ", e);
            throw new BaseException("Ошибка аутентификации.", e);
        }
    }

    @Override
    public BigDecimal getBalance(CryptoCurrency cryptoCurrency) {
        if (requestAuthorizationHeader.isEmpty()) {
            authenticate();
        }
        try {
            ResponseEntity<ApiResponse<BigDecimal>> response = requestService.get(
                    balanceUrl,
                    requestAuthorizationHeader,
                    RequestParam.builder().key("cryptoCurrency").value(cryptoCurrency.name()).build()
            );
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при получении баланса для {} пустое.", cryptoCurrency.name());
                throw new BaseException("В ответе должно присутствовать тело.");
            }
            return Objects.requireNonNull(response.getBody()).getData();
        } catch (HttpClientErrorException.Forbidden exception) {
            try {
                requestAuthorizationHeader.clearValue();
                return getBalance(cryptoCurrency);
            } catch (HttpClientErrorException.Forbidden ex) {
                throw new BaseException("Два раза отказано в доступе при получении баланса.", ex);
            }
        }
    }
}
