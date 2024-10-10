package tgb.btc.library.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

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
                return getLitecoinWalletBalance();
            case BITCOIN:
                return getBitcoinWalletBalance();
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
                    sendLtc(deal);
                    break;
                case BITCOIN:
                    sendBtc(deal);
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
            switch (cryptoCurrency) {
                case BITCOIN:
                    sendBtc(deals);
                    break;
                default:
                    throw new BaseException("Для данной криптовалюты не предусмотрен автовывод нескольких сделок.");
            }
        } catch (Exception e) {
            log.error("Ошибка при попытке автовывода сделки {}.", dealPids);
            log.error(e.getMessage(), e);
            throw new BaseException("Ошибка при попытке автовывода.");
        }
    }

    private BigDecimal getLitecoinWalletBalance() {
        if (configPropertiesReader.isDev() || !autoWithdrawalLitecoin) {
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
        headers.setBasicAuth(rpcLitecoinUsername, rpcLitecoinPassword);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(electrumLitecoinRpcUrl, HttpMethod.POST, request, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");

        return new BigDecimal((String) result.get("confirmed"));
    }

    private BigDecimal getBitcoinWalletBalance() {
        if (configPropertiesReader.isDev() || !autoWithdrawalBitcoin) {
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
        headers.setBasicAuth(rpcBitcoinUsername, rpcBitcoinPassword);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(params, headers);

        ResponseEntity<Map> response = restTemplate.exchange(electrumBitcoinRpcUrl, HttpMethod.POST, request, Map.class);

        Map<String, Object> result = (Map<String, Object>) response.getBody().get("result");

        return new BigDecimal((String) result.get("confirmed"));
    }

    private synchronized void sendLtc(Deal deal) throws IOException {
        if (!autoWithdrawalLitecoin) {
            throw new BaseException("Автовывод лайткоина отключен.");
        }
        if (DealStatus.CONFIRMED.equals(dealPropertyService.getDealStatusByPid(deal.getPid()))) {
            throw new BaseException("Заявка уже подтверждена. Автовывод невозможен.");
        }
        if (configPropertiesReader.isDev()) {
            log.debug("Включен режим разработчика. Отправка валюты отменена.");
            return;
        }
        String toAddress = deal.getWallet();
        String amount = deal.getCryptoAmount().toPlainString();
        log.debug("Запрос на автовывод сделки {}, cryptoAmount={}, address={}", deal.getPid(), amount, toAddress);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(electrumLitecoinRpcUrl);

        // Настройка аутентификации
        String auth = rpcLitecoinUsername + ":" + rpcLitecoinPassword;
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

        // Выполнение запроса для создания транзакции
        String signedTransaction;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            Map<String, Object> result = objectMapper.readValue(jsonResponse, Map.class);
            signedTransaction = (String) result.get("result");
            log.info("Транзакция сделки {} создана: {}", deal.getPid(), signedTransaction);
        }

        // Теперь нужно отправить созданную транзакцию в сеть
        HttpPost broadcastPost = new HttpPost(electrumLitecoinRpcUrl);

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
            Map<String, Object> broadcastResult = objectMapper.readValue(broadcastJsonResponse, Map.class);
            log.info("Транзакция сделки {} отправлена. Ответ: {}", deal.getPid(), broadcastResult);
        }
    }

    private synchronized void sendBtc(Deal deal) throws IOException {
        if (!autoWithdrawalBitcoin) {
            throw new BaseException("Автовывод BTC отключен.");
        }
        if (DealStatus.CONFIRMED.equals(dealPropertyService.getDealStatusByPid(deal.getPid()))) {
            throw new BaseException("Заявка уже подтверждена. Автовывод невозможен.");
        }
        if (configPropertiesReader.isDev()) {
            log.debug("Включен режим разработчика. Отправка валюты отменена.");
            return;
        }
        String toAddress = deal.getWallet();
        String amount = deal.getCryptoAmount().toPlainString();
        log.debug("Запрос на автовывод сделки {}, cryptoAmount={}, address={}", deal.getPid(), amount, toAddress);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(electrumBitcoinRpcUrl);

        // Настройка аутентификации
        String auth = rpcBitcoinUsername + ":" + rpcBitcoinUsername;
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

        // Выполнение запроса для создания транзакции
        String signedTransaction;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            Map<String, Object> result = objectMapper.readValue(jsonResponse, Map.class);
            signedTransaction = (String) result.get("result");
            log.info("Транзакция сделки {} создана: {}", deal.getPid(), signedTransaction);
        }

        // Теперь нужно отправить созданную транзакцию в сеть
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

        // Выполнение запроса для отправки транзакции
        try (CloseableHttpResponse broadcastResponse = httpClient.execute(broadcastPost)) {
            String broadcastJsonResponse = EntityUtils.toString(broadcastResponse.getEntity());
            Map<String, Object> broadcastResult = objectMapper.readValue(broadcastJsonResponse, Map.class);
            log.info("Транзакция сделки {} отправлена. Ответ: {}", deal.getPid(), broadcastResult);
        }
    }

    private synchronized void sendBtc(List<Deal> deals) throws IOException {
        if (!autoWithdrawalBitcoin) {
            throw new BaseException("Автовывод BTC отключен.");
        }
        if (configPropertiesReader.isDev()) {
            log.debug("Включен режим разработчика. Отправка валюты отменена.");
            return;
        }
        // Проверка статуса каждой сделки
        for (Deal deal : deals) {
            if (DealStatus.CONFIRMED.equals(dealPropertyService.getDealStatusByPid(deal.getPid()))) {
                throw new BaseException("Заявка уже подтверждена. Автовывод невозможен для сделки " + deal.getPid());
            }
        }

        // Сбор адресов и сумм для отправки
        List<List<Object>> outputsList = new ArrayList<>();
        for (Deal deal : deals) {
            String toAddress = deal.getWallet();
            String amount = deal.getCryptoAmount().toPlainString();
            outputsList.add(Arrays.asList(toAddress, amount));
            log.debug("Добавление сделки {} в транзакцию: amount={}, address={}", deal.getPid(), amount, toAddress);
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(electrumBitcoinRpcUrl);

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

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        httpPost.setEntity(new StringEntity(jsonRequest));
        httpPost.setHeader("Content-type", "application/json");

        // Выполнение запроса для создания транзакции
        String signedTransaction;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            Map<String, Object> result = objectMapper.readValue(jsonResponse, Map.class);
            signedTransaction = (String) result.get("result");
            log.info("Транзакция создана: {}", signedTransaction);
        }

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

        // Выполнение запроса для отправки транзакции
        try (CloseableHttpResponse broadcastResponse = httpClient.execute(broadcastPost)) {
            String broadcastJsonResponse = EntityUtils.toString(broadcastResponse.getEntity());
            Map<String, Object> broadcastResult = objectMapper.readValue(broadcastJsonResponse, Map.class);
            log.info("Транзакция отправлена. Ответ: {}", broadcastResult);
        }
    }
}
