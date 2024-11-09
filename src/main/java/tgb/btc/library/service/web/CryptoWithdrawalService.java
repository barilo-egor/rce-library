package tgb.btc.library.service.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.exception.ApiResponseErrorException;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.web.ICryptoWithdrawalService;
import tgb.btc.library.interfaces.web.IRequestService;
import tgb.btc.library.vo.web.ApiResponse;
import tgb.btc.library.vo.web.PoolDeal;
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

    private final String isOnUrl;

    private final String poolUrl;

    private final String deleteAllPoolUrl;

    private final String completePoolUrl;

    private final List<RequestParam> authenticateParams;

    private final RequestHeader requestAuthorizationHeader;

    private final IRequestService requestService;

    private int balanceAttemptsCount = 0;

    private int withdrawalAttemptsCount = 0;

    private int isOnAttemptsCount = 0;

    private int getAllPoolDealsAttemptsCount = 0;

    private int addPoolDealAttemptsCount = 0;

    private int deletePoolDealAttemptsCount = 0;

    private int deleteAllPoolDealsAttemptsCount = 0;

    private int completePoolAttemptsCount = 0;

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
        isOnUrl = cryptoWithdrawalUrl + "/isOn";
        String pool = "/pool";
        poolUrl = cryptoWithdrawalUrl + pool;
        deleteAllPoolUrl = cryptoWithdrawalUrl + pool + "/all";
        completePoolUrl = cryptoWithdrawalUrl + pool + "/complete";
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
                balanceAttemptsCount = 0;
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
                    throw new ApiResponseErrorException("Ошибка в ответе при авто выводе: " + response.getBody().getError().getMessage());
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
                withdrawalAttemptsCount = 0;
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
                    throw new ApiResponseErrorException("Ошибка в ответе при авто выводе: " + response.getBody().getError().getMessage());
                }
                return response.getBody().getData();
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке авто вывода: ", exception);
                log.debug("Выполняется повторная попытка авто вывода.");
                requestAuthorizationHeader.clearValue();
                return withdrawal(cryptoCurrency, amount, address);
            }
        } catch (Exception e) {
            log.error("Ошибка при попытке автовывода:", e);
            withdrawalAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке автовывода.", e);
        }
    }

    @Override
    public synchronized boolean isOn(CryptoCurrency cryptoCurrency) {
        try {
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            if (isOnAttemptsCount >= maxAttemptsCount) {
                isOnAttemptsCount = 0;
                throw new BaseException("Не удается совершить авто вывод после " + maxAttemptsCount + " попыток.");
            }
            isOnAttemptsCount++;
            ResponseEntity<ApiResponse<Boolean>> response;
            try {
                response = requestService.get(
                        isOnUrl,
                        requestAuthorizationHeader,
                        RequestParam.builder().key("cryptoCurrency").value(cryptoCurrency.name()).build(),
                        Boolean.class
                );
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при узнать включен ли автовывод для криптовалюты.", exception);
                log.debug("Выполняется повторная попытка узнать включен ли автовывод для криптовалюты {}.", cryptoCurrency.name());
                requestAuthorizationHeader.clearValue();
                return isOn(cryptoCurrency);
            }
            isOnAttemptsCount = 0;
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при попытке узнать включен ли автовывод для криптовалюты {} пустое.",
                        cryptoCurrency.name());
                throw new BaseException("В ответе должно присутствовать тело.");
            }
            if (Objects.nonNull(response.getBody().getError())) {
                log.error("Ошибка в ответе при попытке узнать включен ли автовывод для криптовалюты {}",
                        response.getBody().getError().getMessage());
                throw new ApiResponseErrorException("Ошибка в ответе при попытке узнать включен ли автовывод для криптовалюты "
                        + cryptoCurrency.name() + ": " + response.getBody().getError().getMessage());
            }
            return response.getBody().getData();
        }  catch (Exception e) {
            log.error("Ошибка при попытке узнать включен ли автовывод для криптовалюты {}.", cryptoCurrency.name());
            log.error("Описание: ", e);
            isOnAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке узнать включен ли автовывод " + cryptoCurrency.name() + ".", e);
        }
    }

    @Override
    public List<PoolDeal> getAllPoolDeals() {
        try {
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            if (getAllPoolDealsAttemptsCount >= maxAttemptsCount) {
                getAllPoolDealsAttemptsCount = 0;
                throw new BaseException("Не удается получить пул после " + maxAttemptsCount + " попыток.");
            }
            getAllPoolDealsAttemptsCount++;
            ResponseEntity<ApiResponse<List<PoolDeal>>> response;
            try {
                response = requestService.get(
                        poolUrl,
                        requestAuthorizationHeader,
                        new ParameterizedTypeReference<>() {}
                );
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке получения пула: ", exception);
                log.debug("Выполняется повторная попытка получения пула.");
                requestAuthorizationHeader.clearValue();
                return getAllPoolDeals();
            }
            getAllPoolDealsAttemptsCount = 0;
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при попытке получить пул пустое.");
                throw new BaseException("В ответе должно присутствовать тело.");
            }
            if (Objects.nonNull(response.getBody().getError())) {
                log.error("Ошибка в ответе при попытке получения пула: {}",
                        response.getBody().getError().getMessage());
                throw new ApiResponseErrorException("Ошибка в ответе при попытке получения пула: "
                        + response.getBody().getError().getMessage());
            }
            return response.getBody().getData();
        }  catch (Exception e) {
            log.error("Ошибка при попытке получения пула.");
            log.error("Описание: ", e);
            getAllPoolDealsAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке получения пула.", e);
        }
    }

    @Override
    public Integer addPoolDeal(PoolDeal poolDeal) {
        try {
            log.debug("Запрос на добавление сделки {} в пул.", poolDeal.getPid());
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            if (addPoolDealAttemptsCount >= maxAttemptsCount) {
                addPoolDealAttemptsCount = 0;
                throw new BaseException("Не удается добавить сделку в пул после " + maxAttemptsCount + " попыток.");
            }
            addPoolDealAttemptsCount++;
            ResponseEntity<ApiResponse<Integer>> response;
            try {
                response = requestService.post(
                        poolUrl,
                        requestAuthorizationHeader,
                        poolDeal,
                        Integer.class
                );
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке добавления сделки в пул: ", exception);
                log.debug("Выполняется повторная попытка добавления сделки в пул.");
                requestAuthorizationHeader.clearValue();
                return addPoolDeal(poolDeal);
            }
            addPoolDealAttemptsCount = 0;
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при попытке добавить сделку в пул пустое.");
                throw new BaseException("В ответе должно присутствовать тело.");
            }
            if (Objects.nonNull(response.getBody().getError())) {
                log.error("Ошибка в ответе при попытке добавления сделки в пул: {}",
                        response.getBody().getError().getMessage());
                throw new ApiResponseErrorException("Ошибка в ответе при попытке добавления сделки в пул: "
                        + response.getBody().getError().getMessage());
            }
            return response.getBody().getData();
        }  catch (Exception e) {
            log.error("Ошибка при попытке добавления сделки в пул.");
            log.error("Описание: ", e);
            addPoolDealAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке добавления сделки в пул.", e);
        }
    }

    @Override
    public Boolean clearPool() {
        try {
            log.debug("Запрос на очистку пула.");
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            if (deleteAllPoolDealsAttemptsCount >= maxAttemptsCount) {
                deleteAllPoolDealsAttemptsCount = 0;
                throw new BaseException("Не удается очистить пул после " + maxAttemptsCount + " попыток.");
            }
            deleteAllPoolDealsAttemptsCount++;
            ResponseEntity<ApiResponse<Boolean>> response;
            try {
                response = requestService.delete(
                        deleteAllPoolUrl,
                        requestAuthorizationHeader,
                        Boolean.class
                );
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке очистки пула: ", exception);
                log.debug("Выполняется повторная попытка очистки пула.");
                requestAuthorizationHeader.clearValue();
                return clearPool();
            }
            deleteAllPoolDealsAttemptsCount = 0;
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при попытке очистки пула пустое.");
                throw new BaseException("В ответе должно присутствовать тело.");
            }
            if (Objects.nonNull(response.getBody().getError())) {
                log.error("Ошибка в ответе при попытке очистки пула: {}",
                        response.getBody().getError().getMessage());
                throw new ApiResponseErrorException("Ошибка в ответе при попытке очистки пула: "
                        + response.getBody().getError().getMessage());
            }
            return response.getBody().getData();
        }  catch (Exception e) {
            log.error("Ошибка при попытке очистки пула.");
            log.error("Описание: ", e);
            deleteAllPoolDealsAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке очистки пула.", e);
        }
    }

    @Override
    public Long deleteFromPool(String bot, Long pid) {
        return deleteFromPool(PoolDeal.builder().pid(pid).bot(bot).build());
    }

    @Override
    public Long deleteFromPool(Long id) {
        return deleteFromPool(PoolDeal.builder().id(id).build());
    }

    @Override
    public Long deleteFromPool(PoolDeal poolDeal) {
        try {
            log.debug("Запрос на удаление сделки {} из пула.", poolDeal);
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            if (deletePoolDealAttemptsCount >= maxAttemptsCount) {
                deletePoolDealAttemptsCount = 0;
                throw new BaseException("Не удается удалить сделку из пула после " + maxAttemptsCount + " попыток.");
            }
            deletePoolDealAttemptsCount++;
            ResponseEntity<ApiResponse<Integer>> response;
            try {
                response = requestService.delete(
                        poolUrl,
                        requestAuthorizationHeader,
                        poolDeal,
                        Integer.class
                );
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке удаления сделки из пула: ", exception);
                log.debug("Выполняется повторная попытка удаления сделки из пула.");
                requestAuthorizationHeader.clearValue();
                return deleteFromPool(poolDeal);
            }
            deletePoolDealAttemptsCount = 0;
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при попытке удаления сделки из пула пустое.");
                throw new BaseException("В ответе должно присутствовать тело.");
            }
            if (Objects.nonNull(response.getBody().getError())) {
                log.error("Ошибка в ответе при попытке удаления сделки из пула: {}",
                        response.getBody().getError().getMessage());
                throw new ApiResponseErrorException("Ошибка в ответе при попытке удаления сделки из пула: "
                        + response.getBody().getError().getMessage());
            }
            return Long.valueOf(response.getBody().getData());
        }  catch (Exception e) {
            log.error("Ошибка при попытке удаления сделки из пула.");
            log.error("Описание: ", e);
            deletePoolDealAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке удаления сделки из пула.", e);
        }
    }

    @Override
    public String complete() {
        try {
            if (requestAuthorizationHeader.isEmpty()) {
                authenticate();
            }
            if (completePoolAttemptsCount >= maxAttemptsCount) {
                completePoolAttemptsCount = 0;
                throw new BaseException("Не удается завершить пул после " + maxAttemptsCount + " попыток.");
            }
            completePoolAttemptsCount++;
            ResponseEntity<ApiResponse<String>> response;
            try {
                response = requestService.post(
                        completePoolUrl,
                        requestAuthorizationHeader,
                        String.class
                );
            } catch (HttpClientErrorException.Forbidden exception) {
                log.debug("Ошибка аутентификации при попытке завершения пула: ", exception);
                log.debug("Выполняется повторная попытка завершения пула.");
                requestAuthorizationHeader.clearValue();
                return complete();
            }
            completePoolAttemptsCount = 0;
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при попытке завершить пул пустое.");
                throw new BaseException("В ответе должно присутствовать тело.");
            }
            if (Objects.nonNull(response.getBody().getError())) {
                log.error("Ошибка в ответе при попытке завершения пула: {}",
                        response.getBody().getError().getMessage());
                throw new ApiResponseErrorException("Ошибка в ответе при попытке завершения пула: "
                        + response.getBody().getError().getMessage());
            }
            return response.getBody().getData();
        }  catch (Exception e) {
            log.error("Ошибка при попытке завершения пула.");
            log.error("Описание: ", e);
            completePoolAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке завершения пула.", e);
        }
    }
}
