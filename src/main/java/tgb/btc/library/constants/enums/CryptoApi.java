package tgb.btc.library.constants.enums;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.exception.ReadFromUrlException;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

@Slf4j
public enum CryptoApi {
    BTC_USD_BLOCKCHAIN("https://api.blockchain.com/v3/exchange/tickers/BTC-USD",
            jsonObject -> {
                try {
                    return jsonObject.get("last_trade_price");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    BTC_USD_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=BTCUSDT",
            jsonObject -> {
                try {
                    return jsonObject.get("price");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    LTC_USD_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=LTCUSDT",
            jsonObject -> {
                try {
                    return jsonObject.get("price");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    LTC_USD_BLOCKCHAIN("https://api.blockchain.com/v3/exchange/tickers/LTC-USD",
            jsonObject -> {
                try {
                    return jsonObject.get("last_trade_price");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    XMR_USD_BINANCE("https://api.binance.com/api/v3/ticker/price?symbol=XMRUSDT",
            jsonObject -> {
                try {
                    return jsonObject.get("price");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    XMR_USD_KRAKEN("https://api.kraken.com/0/public/Ticker?pair=XMRUSD",
            jsonObject -> {
                try {
                    return ((JSONArray) ((JSONObject) ((JSONObject) jsonObject.get("result")).get("XXMRZUSD")).get(
                            "a")).get(0);
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    BTC_RUB_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=BTCRUB",
            jsonObject -> {
                try {
                    return jsonObject.get("price");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    LTC_RUB_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=LTCRUB",
            jsonObject -> {
                try {
                    return jsonObject.get("price");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    BTC_RUB_COINGECKO("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=rub",
            jsonObject -> {
                try {
                    return ((JSONObject) jsonObject.get("bitcoin")).get("rub");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    LTC_RUB_COINGECKO("https://api.coingecko.com/api/v3/simple/price?ids=litecoin&vs_currencies=rub",
            jsonObject -> {
                try {
                    return ((JSONObject) jsonObject.get("litecoin")).get("rub");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            }),
    USD_RUB_EXCHANGERATE("https://v6.exchangerate-api.com/v6/8ae628548cbe656cdc6f0a9e/latest/USD",
            jsonObject -> {
                try {
                    return ((JSONObject) jsonObject.get("conversion_rates")).get("RUB");
                } catch (JSONException e) {
                    throw new BaseException("Ошибка при парсинге json курса.");
                }
            });

    final String url;

    final Function<JSONObject, Object> currencyReceiver;

    CryptoApi(String url, Function<JSONObject, Object> currencyReceiver) {
        this.url = url;
        this.currencyReceiver = currencyReceiver;
    }

    public static final List<CryptoApi> BTC_USD = List.of(CryptoApi.BTC_USD_BINANCE, CryptoApi.BTC_USD_BLOCKCHAIN);

    public static final List<CryptoApi> LTS_USD = List.of(CryptoApi.LTC_USD_BINANCE, CryptoApi.LTC_USD_BLOCKCHAIN);

    public static final List<CryptoApi> XMR_USD = List.of(CryptoApi.XMR_USD_KRAKEN);

    public static final List<CryptoApi> BTC_RUB = List.of(CryptoApi.BTC_RUB_BINANCE, CryptoApi.BTC_RUB_COINGECKO);

    public static final List<CryptoApi> LTC_RUB = List.of(CryptoApi.LTC_RUB_BINANCE, CryptoApi.LTC_RUB_COINGECKO);

    public static List<CryptoApi> getCryptoApis(CryptoCurrency cryptoCurrency) {
        switch (cryptoCurrency) {
            case BITCOIN:
                return BTC_USD;
            case LITECOIN:
                return LTS_USD;
            case MONERO:
                return XMR_USD;
        }
        throw new BaseException("API USD для криптовалюты " + cryptoCurrency.name() + " не предусмотрены.");
    }

    public static List<CryptoApi> getFiatCryptoApis(CryptoCurrency cryptoCurrency) {
        switch (cryptoCurrency) {
            case BITCOIN:
                return BTC_RUB;
            case LITECOIN:
                return LTC_RUB;
        }
        throw new BaseException("API RUB для криптовалюты " + cryptoCurrency.name() + " не предусмотрены.");
    }

    public BigDecimal getCourse() {
        return parse(this.currencyReceiver.apply(readJsonFromUrl(this.url)));
    }

    private static BigDecimal parse(Object price) {
        double parsedPrice;
        if (price instanceof String)
            parsedPrice = Double.parseDouble((String) price);
        else if (price instanceof Double)
            parsedPrice = (Double) price;
        else if (price instanceof BigDecimal)
            parsedPrice = ((BigDecimal) price).doubleValue();
        else if (price instanceof Integer)
            parsedPrice = ((Integer) price).doubleValue();
        else
            throw new BaseException("Не определен тип значения из JSON.");
        return BigDecimal.valueOf(parsedPrice);
    }

    private static JSONObject readJsonFromUrl(String url) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(5000);
        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url,
                    String.class);
            return new JSONObject(response.getBody());
        } catch (Exception ex) {
            log.error("Ошибка при получении курса по url=" + url, ex);
            throw new ReadFromUrlException(
                    "Проблема при получении курса. Создание заявки для этой валюты пока что невозможно.");
        }
    }
}
