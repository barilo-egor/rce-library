package tgb.btc.library.constants.enums;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.exception.ReadFromUrlException;

import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

@Slf4j
public enum CryptoApi {
    BTC_USD_BLOCKCHAIN("https://api.blockchain.com/v3/exchange/tickers/BTC-USD",
            jsonObject -> jsonObject.get("last_trade_price")),
    BTC_USD_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=BTCUSDT",
            jsonObject -> jsonObject.get("price")),
    LTC_USD_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=LTCUSDT",
            jsonObject -> jsonObject.get("price")),
    BTC_RUB_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=BTCRUB",
            jsonObject -> jsonObject.get("price")),
    LTC_RUB_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=LTCRUB",
            jsonObject -> jsonObject.get("price")),
    LTC_RUB_COINREMITTER("https://min-api.cryptocompare.com/data/price?fsym=LTC&tsyms=RUB",
            jsonObject -> jsonObject.get("RUB")),
    XMR_USD_COINREMITTER("https://min-api.cryptocompare.com/data/price?fsym=XMR&tsyms=BTC,USD,EUR",
            jsonObject -> jsonObject.get("USD")),
    //    XMR_USD_COINREMITTER("https://coinremitter.com/api/v3/get-coin-rate",
    //                         jsonObject -> ((JSONObject) ((JSONObject) jsonObject.get("data")).get("XMR")).get("price"))
    USD_RUB_EXCHANGERATE("https://v6.exchangerate-api.com/v6/8ae628548cbe656cdc6f0a9e/latest/USD",
            jsonObject -> ((JSONObject) jsonObject.get("conversion_rates")).get("RUB"))
    ;

    final String url;

    final Function<JSONObject, Object> currencyReceiver;

    CryptoApi(String url, Function<JSONObject, Object> currencyReceiver) {
        this.url = url;
        this.currencyReceiver = currencyReceiver;
    }

    public static final List<CryptoApi> BTC_USD = List.of(CryptoApi.BTC_USD_BINANCE, CryptoApi.BTC_USD_BLOCKCHAIN);

    public BigDecimal getCourse() {
        return parse(this.currencyReceiver.apply(readJsonFromUrl(this.url)));
    }

    private static BigDecimal parse(Object price) {
        double parsedPrice;
        if (price instanceof String) parsedPrice = Double.parseDouble((String) price);
        else if (price instanceof Double) parsedPrice = (Double) price;
        else if (price instanceof BigDecimal) parsedPrice = ((BigDecimal) price).doubleValue();
        else if (price instanceof Integer) parsedPrice = ((Integer) price).doubleValue();
        else throw new BaseException("Не определен тип значения из JSON.");
        return BigDecimal.valueOf(parsedPrice);
    }

    private static JSONObject readJsonFromUrl(String url) {
        log.debug("Открытие стрима для получения курса.");
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url,
                String.class);
        try {
            return new JSONObject(response.getBody());
        } catch (Exception ex) {
            log.error("Ошибка при получении курса по url=" + url, ex);
            throw new ReadFromUrlException("Проблема при получении курса. Создание заявки для этой валюты пока что невозможно.");
        }
    }
}
