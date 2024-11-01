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
import tgb.btc.library.vo.web.electrum.WithdrawalRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class CryptoWithdrawalService implements ICryptoWithdrawalService {

    private final String authenticateUrl;

    private final String balanceUrl;

    private final String withdrawalUrl;

    private final List<RequestParam> authenticateParams;

    private final RequestHeader requestAuthorizationHeader;

    private final IRequestService requestService;

    private int balanceAttemptsCount = 0;

    private int withdrawalAttemptsCount = 0;

    private final int maxAttemptsCount = 3;

    @Autowired
    public CryptoWithdrawalService(IRequestService requestService,
                                   @Value("${crypto-withdrawal.url}") String cryptoWithdrawalUrl,
                                   @Value("${crypto-withdrawal.username}") String username,
                                   @Value("${crypto-withdrawal.password}") String password) {
        this.requestService = requestService;
        authenticateUrl = cryptoWithdrawalUrl + "/authenticate";
        balanceUrl = cryptoWithdrawalUrl + "/balance";
        withdrawalUrl = cryptoWithdrawalUrl + "/withdrawal";
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
    public synchronized BigDecimal getBalance(CryptoCurrency cryptoCurrency) {
        try {
            if (balanceAttemptsCount >= maxAttemptsCount) {
                throw new BaseException("Не удается получить баланс после " + maxAttemptsCount + " попыток.");
            }
            log.debug("Выполнение запроса на получение баланса кошелька {}", cryptoCurrency.name());
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            try {
                balanceAttemptsCount++;
                ResponseEntity<ApiResponse<Double>> response = requestService.get(
                        balanceUrl,
                        requestAuthorizationHeader,
                        RequestParam.builder().key("cryptoCurrency").value(cryptoCurrency.name()).build(),
                        Double.class
                );
                balanceAttemptsCount = 0;
                if (Objects.isNull(response.getBody())) {
                    log.error("Тело ответа при получении баланса для {} пустое.", cryptoCurrency.name());
                    throw new BaseException("В ответе должно присутствовать тело.");
                }
                if (Objects.nonNull(response.getBody().getError())) {
                    log.error("Ошибка в ответе при получении баланса: {}", response.getBody().getError().getMessage());
                    throw new BaseException("Ошибка в ответе при авто выводе: " + response.getBody().getError().getMessage());
                }
                return BigDecimal.valueOf(response.getBody().getData());
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке получения баланса: ", exception);
                log.debug("Выполняется повторная попытка. получения баланса");
                requestAuthorizationHeader.clearValue();
                authenticate();
                return getBalance(cryptoCurrency);
            }
        } catch (Exception e) {
            log.error("Ошибка при попытке получения баланса:", e);
            balanceAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке получения баланса.", e);
        }
    }

    @Override
    public synchronized String withdrawal(CryptoCurrency cryptoCurrency, BigDecimal amount, String address) {
        try {
            if (withdrawalAttemptsCount >= maxAttemptsCount) {
                throw new BaseException("Не удается совершить авто вывод после " + maxAttemptsCount + " попыток.");
            }
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            try {
                withdrawalAttemptsCount++;
                ResponseEntity<ApiResponse<String>> response = requestService.post(
                        withdrawalUrl,
                        requestAuthorizationHeader,
                        WithdrawalRequest.builder().cryptoCurrency(cryptoCurrency).amount(amount.toPlainString())
                                .address(address).build(),
                        String.class
                );
                withdrawalAttemptsCount = 0;
                if (Objects.isNull(response.getBody())) {
                    log.error("Тело ответа при попытке авто вывода для {} пустое.Address={}, amount={}",
                            cryptoCurrency.name(), address, amount);
                    throw new BaseException("В ответе должно присутствовать тело.");
                }
                if (Objects.nonNull(response.getBody().getError())) {
                    log.error("Ошибка в ответе при попытке авто вывода: {}", response.getBody().getError().getMessage());
                    throw new BaseException("Ошибка в ответе при авто выводе: " + response.getBody().getError().getMessage());
                }
                return response.getBody().getData();
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке авто вывода: ", exception);
                log.debug("Выполняется повторная попытка авто вывода.");
                requestAuthorizationHeader.clearValue();
                withdrawal(cryptoCurrency, amount, address);
            }
        } catch (Exception e) {
            log.error("Ошибка при попытке автовывода:", e);
            withdrawalAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке автовывода.", e);
        }
        throw new BaseException("Непредвиденное поведение метода.");
    }
}
