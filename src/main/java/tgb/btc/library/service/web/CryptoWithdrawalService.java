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
import java.util.*;

@Service
@Slf4j
public class CryptoWithdrawalService implements ICryptoWithdrawalService {

    private static final String ATTEMPTS_STRING = " попыток.";

    private static final String RESPONSE_BODY_REQUIRED = "В ответе должно присутствовать тело.";

    private static final String DESCRIPTION = "Описание: ";

    private final String authenticateUrl;

    private final String balanceUrl;

    private final String withdrawalUrl;

    private final String isOnUrl;

    private final String poolUrl;

    private final String deleteAllPoolUrl;

    private final String completePoolUrl;

    private final String walletUrl;

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

    private int changeWalletAttemptsCount = 0;

    private static final int MAX_ATTEMPTS_COUNT = 3;

    public static final String AUTO = "Авто";

    /**
     * Последняя введенная оператором комиссия в sat/vB (целое число сатоши/байт)
     */
    private final Map<CryptoCurrency, String> lastFeeRate;

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
        walletUrl = cryptoWithdrawalUrl + "/wallet/%s";
        authenticateParams = new ArrayList<>();
        authenticateParams.add(RequestParam.builder().key("username").value(username).build());
        authenticateParams.add(RequestParam.builder().key("password").value(password).build());
        requestAuthorizationHeader = new RequestHeader();
        requestAuthorizationHeader.setName("Authorization");
        lastFeeRate = new EnumMap<>(CryptoCurrency.class);
        for (CryptoCurrency cryptoCurrency : CryptoCurrency.values()) {
            lastFeeRate.put(cryptoCurrency, AUTO);
        }
        log.debug("Сервис для взаимодействия с микросервисом crypto-withdrawal успешно загружен в контекст. Url = {}", cryptoWithdrawalUrl);
    }

    @Override
    public String getAutoName() {
        return AUTO;
    }

    @Override
    public boolean isAutoFeeRate(CryptoCurrency cryptoCurrency) {
        return lastFeeRate.get(cryptoCurrency).equals(AUTO);
    }

    @Override
    public String getFeeRate(CryptoCurrency cryptoCurrency) {
        return lastFeeRate.get(cryptoCurrency);
    }

    @Override
    public void putFeeRate(CryptoCurrency cryptoCurrency, String feeRate) {
        lastFeeRate.put(cryptoCurrency, feeRate);
    }

    @Override
    public void putAutoFeeRate(CryptoCurrency cryptoCurrency) {
        lastFeeRate.put(cryptoCurrency, AUTO);
    }

    private synchronized void authenticate() {
        if (!requestAuthorizationHeader.isEmpty()) {
            return;
        }
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
            if (balanceAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                balanceAttemptsCount = 0;
                throw new BaseException("Не удается получить баланс после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            log.debug("Выполнение запроса на получение баланса кошелька {}", cryptoCurrency.name());
            authenticate();
            return makeGetBalanceRequest(cryptoCurrency);
        } catch (Exception e) {
            log.error("Ошибка при попытке получения баланса:", e);
            balanceAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке получения баланса.", e);
        }
    }

    private BigDecimal makeGetBalanceRequest(CryptoCurrency cryptoCurrency) {
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
                throw new BaseException(RESPONSE_BODY_REQUIRED);
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
    }

    @Override
    public synchronized String withdrawal(CryptoCurrency cryptoCurrency, BigDecimal amount, String address) {
        try {
            if (withdrawalAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                withdrawalAttemptsCount = 0;
                throw new BaseException("Не удается совершить авто вывод после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            authenticate();
            return makeWithdrawalRequest(cryptoCurrency, amount, address);
        } catch (Exception e) {
            log.error("Ошибка при попытке автовывода:", e);
            withdrawalAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке автовывода.", e);
        }
    }

    private String makeWithdrawalRequest(CryptoCurrency cryptoCurrency, BigDecimal amount, String address) {
        try {
            withdrawalAttemptsCount++;
            ResponseEntity<ApiResponse<String>> response = requestService.post(
                    withdrawalUrl,
                    requestAuthorizationHeader,
                    WithdrawalRequest.builder().cryptoCurrency(cryptoCurrency).amount(amount.toPlainString())
                            .address(address).fee(isAutoFeeRate(cryptoCurrency) ? null : getFeeRate(cryptoCurrency)).build(),
                    String.class
            );
            withdrawalAttemptsCount = 0;
            if (Objects.isNull(response.getBody())) {
                log.error("Тело ответа при попытке авто вывода для {} пустое.Address={}, amount={}",
                        cryptoCurrency.name(), address, amount);
                throw new BaseException(RESPONSE_BODY_REQUIRED);
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
    }

    @Override
    public synchronized boolean isOn(CryptoCurrency cryptoCurrency) {
        try {
            if (isOnAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                isOnAttemptsCount = 0;
                throw new BaseException("Не удается совершить авто вывод после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            authenticate();
            isOnAttemptsCount++;
            return makeIsOnRequest(cryptoCurrency);
        }  catch (Exception e) {
            log.error("Ошибка при попытке узнать включен ли автовывод для криптовалюты {}.", cryptoCurrency.name());
            log.error(DESCRIPTION, e);
            isOnAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке узнать включен ли автовывод " + cryptoCurrency.name() + ".", e);
        }
    }

    private boolean makeIsOnRequest(CryptoCurrency cryptoCurrency) {
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
            throw new BaseException(RESPONSE_BODY_REQUIRED);
        }
        if (Objects.nonNull(response.getBody().getError())) {
            log.error("Ошибка в ответе при попытке узнать включен ли автовывод для криптовалюты {}",
                    response.getBody().getError().getMessage());
            throw new ApiResponseErrorException("Ошибка в ответе при попытке узнать включен ли автовывод для криптовалюты "
                    + cryptoCurrency.name() + ": " + response.getBody().getError().getMessage());
        }
        return response.getBody().getData();
    }

    @Override
    public List<PoolDeal> getAllPoolDeals() {
        try {
            if (getAllPoolDealsAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                getAllPoolDealsAttemptsCount = 0;
                throw new BaseException("Не удается получить пул после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            authenticate();
            getAllPoolDealsAttemptsCount++;
            return makeGetAllPoolDealsRequest();
        }  catch (Exception e) {
            log.error("Ошибка при попытке получения пула.");
            log.error(DESCRIPTION, e);
            getAllPoolDealsAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке получения пула.", e);
        }
    }

    private List<PoolDeal> makeGetAllPoolDealsRequest() {
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
            throw new BaseException(RESPONSE_BODY_REQUIRED);
        }
        if (Objects.nonNull(response.getBody().getError())) {
            log.error("Ошибка в ответе при попытке получения пула: {}",
                    response.getBody().getError().getMessage());
            throw new ApiResponseErrorException("Ошибка в ответе при попытке получения пула: "
                    + response.getBody().getError().getMessage());
        }
        return response.getBody().getData();
    }

    @Override
    public Integer addPoolDeal(PoolDeal poolDeal) {
        try {
            log.debug("Запрос на добавление сделки {} в пул.", poolDeal.getPid());
            authenticate();
            if (addPoolDealAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                addPoolDealAttemptsCount = 0;
                throw new BaseException("Не удается добавить сделку в пул после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            addPoolDealAttemptsCount++;
            return makeAddPoolDealRequest(poolDeal);
        }  catch (Exception e) {
            log.error("Ошибка при попытке добавления сделки в пул.");
            log.error(DESCRIPTION, e);
            addPoolDealAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке добавления сделки в пул.", e);
        }
    }

    private Integer makeAddPoolDealRequest(PoolDeal poolDeal) {
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
            throw new BaseException(RESPONSE_BODY_REQUIRED);
        }
        if (Objects.nonNull(response.getBody().getError())) {
            log.error("Ошибка в ответе при попытке добавления сделки в пул: {}",
                    response.getBody().getError().getMessage());
            throw new ApiResponseErrorException("Ошибка в ответе при попытке добавления сделки в пул: "
                    + response.getBody().getError().getMessage());
        }
        return response.getBody().getData();
    }

    @Override
    public Boolean clearPool() {
        try {
            log.debug("Запрос на очистку пула.");
            authenticate();
            if (deleteAllPoolDealsAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                deleteAllPoolDealsAttemptsCount = 0;
                throw new BaseException("Не удается очистить пул после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            deleteAllPoolDealsAttemptsCount++;
            return makeClearPoolRequest();
        }  catch (Exception e) {
            log.error("Ошибка при попытке очистки пула.");
            log.error(DESCRIPTION, e);
            deleteAllPoolDealsAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке очистки пула.", e);
        }
    }

    private Boolean makeClearPoolRequest() {
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
            throw new BaseException(RESPONSE_BODY_REQUIRED);
        }
        if (Objects.nonNull(response.getBody().getError())) {
            log.error("Ошибка в ответе при попытке очистки пула: {}",
                    response.getBody().getError().getMessage());
            throw new ApiResponseErrorException("Ошибка в ответе при попытке очистки пула: "
                    + response.getBody().getError().getMessage());
        }
        return response.getBody().getData();
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
            authenticate();
            if (deletePoolDealAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                deletePoolDealAttemptsCount = 0;
                throw new BaseException("Не удается удалить сделку из пула после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            deletePoolDealAttemptsCount++;
            return makeDeleteFromPoolRequest(poolDeal);
        }  catch (Exception e) {
            log.error("Ошибка при попытке удаления сделки из пула.");
            log.error(DESCRIPTION, e);
            deletePoolDealAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке удаления сделки из пула.", e);
        }
    }

    private Long makeDeleteFromPoolRequest(PoolDeal poolDeal) {
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
            throw new BaseException(RESPONSE_BODY_REQUIRED);
        }
        if (Objects.nonNull(response.getBody().getError())) {
            log.error("Ошибка в ответе при попытке удаления сделки из пула: {}",
                    response.getBody().getError().getMessage());
            throw new ApiResponseErrorException("Ошибка в ответе при попытке удаления сделки из пула: "
                    + response.getBody().getError().getMessage());
        }
        return Long.valueOf(response.getBody().getData());
    }

    @Override
    public String complete() {
        try {
            authenticate();
            if (completePoolAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                completePoolAttemptsCount = 0;
                throw new BaseException("Не удается завершить пул после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            completePoolAttemptsCount++;
            return makeCompleteRequest();
        }  catch (Exception e) {
            log.error("Ошибка при попытке завершения пула.");
            log.error(DESCRIPTION, e);
            completePoolAttemptsCount = 0;
            throw new BaseException("Ошибка при попытке завершения пула.", e);
        }
    }

    private String makeCompleteRequest() {
        ResponseEntity<ApiResponse<String>> response;
        try {
            response = requestService.post(
                    completePoolUrl,
                    requestAuthorizationHeader,
                    RequestParam.builder().key("fee").value(lastFeeRate.get(CryptoCurrency.BITCOIN)).build(),
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
            throw new BaseException(RESPONSE_BODY_REQUIRED);
        }
        if (Objects.nonNull(response.getBody().getError())) {
            log.error("Ошибка в ответе при попытке завершения пула: {}",
                    response.getBody().getError().getMessage());
            throw new ApiResponseErrorException("Ошибка в ответе при попытке завершения пула: "
                    + response.getBody().getError().getMessage());
        }
        return response.getBody().getData();
    }

    @Override
    public void changeWallet(CryptoCurrency cryptoCurrency, String seedPhrase) {
        if (Objects.isNull(seedPhrase) || seedPhrase.isEmpty()) {
            throw new BaseException("Сид фраза не может быть пуста.");
        }
        if (Objects.isNull(cryptoCurrency)) {
            throw new BaseException("Не указана криптовалюта.");
        }
        ResponseEntity<ApiResponse<Object>> response;
        try {
            authenticate();
            if (changeWalletAttemptsCount >= MAX_ATTEMPTS_COUNT) {
                changeWalletAttemptsCount = 0;
                throw new BaseException("Не удается заменить кошелек после " + MAX_ATTEMPTS_COUNT + ATTEMPTS_STRING);
            }
            changeWalletAttemptsCount++;
            response = requestService.post(
                    String.format(walletUrl, cryptoCurrency.name()),
                    requestAuthorizationHeader,
                    seedPhrase,
                    Object.class
            );
        } catch (HttpClientErrorException.Forbidden exception) {
            log.debug("Ошибка аутентификации при попытке замены кошелька: ", exception);
            log.debug("Выполняется повторная попытка замены кошелька");
            requestAuthorizationHeader.clearValue();
            changeWallet(cryptoCurrency, seedPhrase);
            return;
        }
        if (Objects.nonNull(response.getBody()) && Objects.nonNull(response.getBody().getError())) {
            log.error("Ошибка в ответе при попытке замены кошелька: {}",
                    response.getBody().getError().getMessage());
            throw new ApiResponseErrorException("Ошибка в ответе при попытке замены кошелька: "
                    + response.getBody().getError().getMessage());
        }
    }
}
