package tgb.btc.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.bean.bot.Deal;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.constants.enums.bot.DealStatus;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.service.IAutoWithdrawalService;
import tgb.btc.library.interfaces.service.bean.bot.deal.IReadDealService;
import tgb.btc.library.interfaces.service.bean.bot.deal.read.IDealPropertyService;
import tgb.btc.library.service.properties.ConfigPropertiesReader;
import tgb.btc.library.vo.web.electrum.GetBalanceElectrumResponse;
import tgb.btc.library.vo.web.electrum.SingleTransactionElectrumResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AutoWithdrawalService implements IAutoWithdrawalService {

    @Value("${electrum.litecoin.rpc.url:#{null}}")
    private String electrumLitecoinRpcUrl;

    @Value("${rpc.litecoin.username:#{null}}")
    private String rpcLitecoinUsername;

    @Value("${rpc.litecoin.password:#{null}}")
    private String rpcLitecoinPassword;

    @Value("${auto.withdrawal.litecoin:#{false}}")
    private boolean autoWithdrawalLitecoin;

    @Value("${electrum.bitcoin.rpc.url:#{null}}")
    private String electrumBitcoinRpcUrl;

    @Value("${rpc.bitcoin.username:#{null}}")
    private String rpcBitcoinUsername;

    @Value("${rpc.bitcoin.password:#{null}}")
    private String rpcBitcoinPassword;


    @Value("${auto.withdrawal.bitcoin:#{false}}")
    private boolean autoWithdrawalBitcoin;

    @Value("${rpc.min.amount:#{null}}")
    private Boolean isMinAmount;

    private final ObjectMapper objectMapper = new ObjectMapper();


    private final IReadDealService readDealService;

    private final ConfigPropertiesReader configPropertiesReader;

    private final IDealPropertyService dealPropertyService;

    @Autowired
    public AutoWithdrawalService(IReadDealService readDealService, ConfigPropertiesReader configPropertiesReader,
                                 IDealPropertyService dealPropertyService) {
        this.readDealService = readDealService;
        this.configPropertiesReader = configPropertiesReader;
        this.dealPropertyService = dealPropertyService;
    }

    @Override
    public BigDecimal getBalance(CryptoCurrency cryptoCurrency) {
        switch (cryptoCurrency) {
            case LITECOIN:
            case BITCOIN:
                return getElectrumBalance(cryptoCurrency);
            default:
                return null;
        }
    }

    @Override
    public void withdrawal(Long dealPid) {
        try {
            Deal deal = readDealService.findByPid(dealPid);
            switch (deal.getCryptoCurrency()) {
                case LITECOIN:
                case BITCOIN:
                    sendElectrum(deal);
                    break;
                default:
                    throw new BaseException("Для данной криптовалюты не предусмотрен автовывод сделки.");
            }
        } catch (Exception e) {
            log.error("Ошибка при попытке автовывода сделки {}.", dealPid);
            log.error(e.getMessage(), e);
            throw new BaseException("Ошибка при попытке автовывода.");
        }
    }

    @Override
    public void withdrawal(List<Long> dealPids) {
        try {
            List<Deal> deals = readDealService.getDealsByPids(dealPids);
            if (CollectionUtils.isEmpty(deals))
                throw new BaseException("Не найдено ни одной сделки по пидам.");
            CryptoCurrency cryptoCurrency = deals.get(0).getCryptoCurrency();
            if (deals.stream().anyMatch(deal -> !cryptoCurrency.equals(deal.getCryptoCurrency())))
                throw new BaseException("Все сделки должны быть одной крипто валюты.");
            if (cryptoCurrency == CryptoCurrency.BITCOIN) {
                sendElectrum(deals);
            } else {
                throw new BaseException("Для данной криптовалюты не предусмотрен автовывод нескольких сделок.");
            }
        } catch (Exception e) {
            log.error("Ошибка при попытке автовывода сделки {}.", dealPids);
            log.error(e.getMessage(), e);
            throw new BaseException("Ошибка при попытке автовывода.");
        }
    }

    private BigDecimal getElectrumBalance(CryptoCurrency cryptoCurrency) {
        if (!CryptoCurrency.ELECTRUM_CURRENCIES.contains(cryptoCurrency)) {
            throw new BaseException("Реализация предусмотрена только для валют через electrum.");
        }
        if (configPropertiesReader.isDev() || !isAutoWithdrawalOn(cryptoCurrency)) {
            log.debug("Включен режим разработчика. Возвращается заглушка для баланса.");
            return new BigDecimal(500);
        }
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);
        String method = "getbalance";

        Map<String, Object> params = new HashMap<>();
        params.put("method", method);
        params.put("params", new Object[]{});
        params.put("id", 1);
        params.put("jsonrpc", "2.0");

        HttpHeaders headers = new HttpHeaders();
        if (CryptoCurrency.BITCOIN.equals(cryptoCurrency)) {
            headers.setBasicAuth(rpcBitcoinUsername, rpcBitcoinPassword);
        } else {
            headers.setBasicAuth(rpcLitecoinUsername, rpcLitecoinPassword);
        }
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

        String url = getUrl(cryptoCurrency);
        ResponseEntity<GetBalanceElectrumResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, request, GetBalanceElectrumResponse.class);
        GetBalanceElectrumResponse response = responseEntity.getBody();
        if (Objects.isNull(response)) {
            throw new BaseException("Отсутствует объект ответа.");
        }
        if (Objects.nonNull(response.getResult()) && StringUtils.isNotBlank(response.getResult().getConfirmed())) {
            return new BigDecimal(response.getResult().getConfirmed());
        } else if (Objects.nonNull(response.getError())) {
            String message = "Ошибка получения баланса для " + cryptoCurrency.getShortName() + ". Сообщение от electrum: " + response.getError().getMessage();
            throw new BaseException(message);
        } else {
            String message = "В ответе при получении баланса отсутствуют confirmed и error.";
            log.error("{}\nОтвет: {}", message, response);
            throw new BaseException(message);
        }
    }

    private synchronized void sendElectrum(Deal deal) throws IOException {
        CryptoCurrency cryptoCurrency = deal.getCryptoCurrency();
        if (!CryptoCurrency.ELECTRUM_CURRENCIES.contains(cryptoCurrency)) {
            throw new BaseException("Реализация предусмотрена только для валют через electrum.");
        }
        if (DealStatus.CONFIRMED.equals(dealPropertyService.getDealStatusByPid(deal.getPid()))) {
            throw new BaseException("Заявка уже подтверждена. Автовывод невозможен.");
        }
        if (configPropertiesReader.isDev() || !isAutoWithdrawalOn(cryptoCurrency)) {
            log.debug("Включен режим разработчика. Фиктивная отправка транзакции.");
            return;
        }
        String url = getUrl(cryptoCurrency);
        String toAddress = deal.getWallet();
        String amount = BooleanUtils.isTrue(isMinAmount) ? "0.00000546" : deal.getCryptoAmount().toPlainString();
        log.debug("Запрос на автовывод сделки {}, cryptoAmount={}, address={}", deal.getPid(), amount, toAddress);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        // Настройка аутентификации
        String auth;
        if (CryptoCurrency.BITCOIN.equals(cryptoCurrency)) {
            auth = rpcBitcoinUsername + ":" + rpcBitcoinPassword;
        } else {
            auth = rpcLitecoinUsername + ":" + rpcLitecoinPassword;
        }
        String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        httpPost.setHeader("Authorization", authHeader);

        // Формирование JSON запроса для создания транзакции
        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("id", "1");
        request.put("method", "payto");

        // Аргументы метода payto
        List<Object> params = new ArrayList<>();
        params.add(toAddress);
        params.add(amount);
        request.put("params", params);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        httpPost.setEntity(new StringEntity(jsonRequest));
        httpPost.setHeader("Content-type", "application/json");

        log.debug("Создание транзакции.");
        String signedTransaction = createTransaction(httpClient, httpPost);

        // Теперь нужно отправить созданную транзакцию в сеть
        HttpPost broadcastPost = new HttpPost(url);

        broadcastPost.setHeader("Authorization", authHeader);

        Map<String, Object> broadcastRequest = new HashMap<>();
        broadcastRequest.put("jsonrpc", "2.0");
        broadcastRequest.put("id", "2");
        broadcastRequest.put("method", "broadcast");
        broadcastRequest.put("params", new String[]{signedTransaction});

        String broadcastJsonRequest = objectMapper.writeValueAsString(broadcastRequest);
        broadcastPost.setEntity(new StringEntity(broadcastJsonRequest));
        broadcastPost.setHeader("Content-type", "application/json");

        // Выполнение запроса для отправки транзакции
        try (CloseableHttpResponse broadcastResponse = httpClient.execute(broadcastPost)) {
            String broadcastJsonResponse = EntityUtils.toString(broadcastResponse.getEntity());
            SingleTransactionElectrumResponse response = objectMapper.readValue(broadcastJsonResponse, SingleTransactionElectrumResponse.class);
            if (Objects.nonNull(response.getError())) {
                log.error("Ошибка при создании транзакции. Ответ: {}", response);
                throw new BaseException("Ошибка при создании транзакции. Код: " + response.getError().getCode()
                        + ", сообщение: " + response.getError().getMessage());
            }
            log.info("Транзакция сделки {} отправлена. Ответ: {}", deal.getPid(), response);
        }
    }

    private String getUrl(CryptoCurrency cryptoCurrency) {
        return CryptoCurrency.BITCOIN.equals(cryptoCurrency)
                ? electrumBitcoinRpcUrl
                : electrumLitecoinRpcUrl;
    }

    @Override
    public boolean isAutoWithdrawalOn(CryptoCurrency cryptoCurrency) {
        switch (cryptoCurrency) {
            case BITCOIN:
                return autoWithdrawalBitcoin;
            case LITECOIN:
                return autoWithdrawalLitecoin;
            default:
                return false;
        }
    }

    private synchronized void sendElectrum(List<Deal> deals) throws IOException {
        if (!autoWithdrawalBitcoin) {
            throw new BaseException("Автовывод BTC отключен.");
        }
        if (configPropertiesReader.isDev()) {
            log.debug("Включен режим разработчика. Отправка валюты отменена.");
            return;
        }
        if (deals.stream().anyMatch(deal -> DealStatus.CONFIRMED.equals(dealPropertyService.getDealStatusByPid(deal.getPid())))) {
            throw new BaseException("Одна из заявок уже подтверждена. Вывод невозможен.");
        }
        if (CollectionUtils.isEmpty(deals)) {
            throw new BaseException("Отсутствуют сделки.");
        }
        log.debug("Запрос на автовывод сделок: {}", deals.stream().map(deal -> deal.getPid().toString()).collect(Collectors.joining(",")));

        // Сбор адресов и сумм для отправки
        List<List<Object>> outputsList = new ArrayList<>();
        for (Deal deal : deals) {
            String toAddress = deal.getWallet();
            String amount = BooleanUtils.isTrue(isMinAmount) ? "0.00000546" : deal.getCryptoAmount().toPlainString();
            outputsList.add(Arrays.asList(toAddress, amount));
            log.debug("Добавление сделки {} в транзакцию: amount={}, address={}", deal.getPid(), amount, toAddress);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(getUrl(deals.get(0).getCryptoCurrency()));

        // Настройка аутентификации
        String auth = rpcBitcoinUsername + ":" + rpcBitcoinPassword; // убедитесь, что используете пароль
        String authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(auth.getBytes());
        httpPost.setHeader("Authorization", authHeader);

        // Формирование запроса JSON-RPC для создания транзакции
        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("id", "1");
        request.put("method", "paytomany");

        // Добавление параметров в запрос
        List<Object> params = new ArrayList<>();
        params.add(outputsList);
        request.put("params", params);
        log.debug("Итоговые параметры: {}", params);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        httpPost.setEntity(new StringEntity(jsonRequest));
        httpPost.setHeader("Content-type", "application/json");

        log.debug("Создание транзакции.");
        String signedTransaction = createTransaction(httpClient, httpPost);

        // Отправка транзакции в сеть
        HttpPost broadcastPost = new HttpPost(electrumBitcoinRpcUrl);

        broadcastPost.setHeader("Authorization", authHeader);

        Map<String, Object> broadcastRequest = new HashMap<>();
        broadcastRequest.put("jsonrpc", "2.0");
        broadcastRequest.put("id", "2");
        broadcastRequest.put("method", "broadcast");
        broadcastRequest.put("params", new String[]{signedTransaction});

        String broadcastJsonRequest = objectMapper.writeValueAsString(broadcastRequest);
        broadcastPost.setEntity(new StringEntity(broadcastJsonRequest));
        broadcastPost.setHeader("Content-type", "application/json");

        log.debug("Выполнение запроса на отправку транзакции {}.", signedTransaction);
        try (CloseableHttpResponse broadcastResponse = httpClient.execute(broadcastPost)) {
            String broadcastJsonResponse = EntityUtils.toString(broadcastResponse.getEntity());
            Map<String, Object> broadcastResult = objectMapper.readValue(broadcastJsonResponse, Map.class);
            if (broadcastResult.containsKey("error")) {
                log.error("Ошибка при автовыводе сделок: {}", deals.stream()
                        .map(deal -> String.valueOf(deal.getPid()))
                        .collect(Collectors.joining())
                );
                throw new BaseException("Ошибка при отправке транзакции: " + broadcastResult.get("error"));
            }
            log.info("Транзакция отправлена. Ответ: {}", broadcastResult);
        } catch (Exception e) {
            log.error("При выполнении запроса на отправку транзакции {} возникла ошибка", signedTransaction);
            log.error("Описание: ", e);
            throw new BaseException("При выполнении запроса на отправку транзакции возникла ошибка.");
        }
    }

    private String createTransaction(CloseableHttpClient httpClient, HttpPost httpPost) throws IOException {
        String signedTransaction;
        try (CloseableHttpResponse httpResponse = httpClient.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(httpResponse.getEntity());
            SingleTransactionElectrumResponse response = objectMapper.readValue(jsonResponse, SingleTransactionElectrumResponse.class);
            if (Objects.nonNull(response.getError())) {
                log.error("Ошибка при создании транзакции. Ответ: {}", response);
                throw new BaseException("Ошибка при создании транзакции. Код: " + response.getError().getCode()
                        + ", сообщение: " + response.getError().getMessage());
            }
            if (StringUtils.isEmpty(response.getResult())) {
                String message = "Отсутствует result при создании транзакции.";
                log.error("{}\nОтвет: {}", message, response);
                throw new BaseException();
            }
            signedTransaction = response.getResult();
            log.info("Транзакция создана: {}", signedTransaction);
        } catch (Exception e) {
            log.error("Ошибка при создании транзакции.", e);
            throw new BaseException("Ошибка при создании транзакции.");
        }
        log.debug("Транзакция создана: {}", signedTransaction);
        return signedTransaction;
    }

    @Override
    public Boolean getMinAmount() {
        return isMinAmount;
    }
}
