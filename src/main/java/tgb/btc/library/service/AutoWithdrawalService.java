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
            }
        } catch (Exception e) {
            log.error("Ошибка при попытке автовывода сделки {}.", dealPid);
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

        // Формирование JSON запроса для создания и отправки транзакции
        Map<String, Object> request = new HashMap<>();
        request.put("jsonrpc", "2.0");
        request.put("id", "1");
        request.put("method", "payto");

        // Аргументы метода payto
        List<Object> params = new ArrayList<>();
        params.add(toAddress);
        params.add(amount);
        // Добавляем параметр broadcast=true
        Map<String, Object> options = new HashMap<>();
        options.put("broadcast", true);
        params.add(options);
        request.put("params", params);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonRequest = objectMapper.writeValueAsString(request);

        httpPost.setEntity(new StringEntity(jsonRequest));
        httpPost.setHeader("Content-type", "application/json");

        // Выполнение запроса для создания и отправки транзакции
        String txHash;
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            Map<String, Object> result = objectMapper.readValue(jsonResponse, Map.class);
            txHash = (String) result.get("result");
            log.info("Транзакция отправлена: {}", txHash);
        }

        // Проверяем статус транзакции
        HttpPost statusPost = new HttpPost(electrumLitecoinRpcUrl);
        statusPost.setHeader("Authorization", authHeader);

        Map<String, Object> statusRequest = new HashMap<>();
        statusRequest.put("jsonrpc", "2.0");
        statusRequest.put("id", "2");
        statusRequest.put("method", "gettransaction");
        statusRequest.put("params", Collections.singletonList(txHash));

        String statusJsonRequest = objectMapper.writeValueAsString(statusRequest);
        statusPost.setEntity(new StringEntity(statusJsonRequest));
        statusPost.setHeader("Content-type", "application/json");

        try (CloseableHttpResponse statusResponse = httpClient.execute(statusPost)) {
            String statusJsonResponse = EntityUtils.toString(statusResponse.getEntity());
            log.debug("Ответ отправки LTC по сделке {}: {}", deal.getPid(), statusJsonResponse);
            Map<String, Object> statusResult = objectMapper.readValue(statusJsonResponse, Map.class);
            String txDetails = (String) statusResult.get("result");

            if (txDetails != null) {
                log.info("Результат отправки LTC по сделке {}: {}", deal.getPid(), txDetails);
            } else {
                log.error("Не получилось вывести LTC по сделке {}.", deal.getPid());
                throw new BaseException("Транзакция не была успешно отправлена.");
            }
        }
    }

}
