package tgb.btc.library.service.enums;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tgb.btc.library.constants.enums.CryptoApi;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.exception.ReadFromUrlException;
import tgb.btc.library.interfaces.enums.ICryptoApiService;

import java.math.BigDecimal;
import java.util.function.Function;

@Service
@Slf4j
public class CryptoApiService implements ICryptoApiService {

    private static final String ERROR_MESSAGE = "Ошибка при парсинге json курса.";

    @Override
    public BigDecimal getCourse(CryptoApi cryptoApi) {
        Function<JSONObject, Object> mapFunction;

        switch (cryptoApi) {
            case BTC_USD_BLOCKCHAIN:
            case LTC_USD_BLOCKCHAIN:
                mapFunction = this::handleBlockchain;
                break;
            case BTC_USD_BINANCE:
            case LTC_USD_BINANCE:
            case XMR_USD_BINANCE:
            case BTC_RUB_BINANCE:
            case LTC_RUB_BINANCE:
                mapFunction = this::handleBinance;
                break;
            case XMR_USD_KRAKEN:
                mapFunction = this::handleKraken;
                break;
            case BTC_RUB_COINGECKO:
                mapFunction = this::handleBtcCoingecko;
                break;
            case LTC_RUB_COINGECKO:
                mapFunction = this::handleLtcCoingecko;
                break;
            case USD_RUB_EXCHANGERATE:
                mapFunction = this::handleExchangerate;
                break;
            default:
                throw new BaseException("Для " + cryptoApi.name() + " нет реализации преобразования JSON.");
        }

        return parse(mapFunction.apply(readJsonFromUrl(cryptoApi.getUrl())));
    }

    private Object handleBlockchain(JSONObject jsonObject) {
        try {
            return jsonObject.get("last_trade_price");
        } catch (JSONException e) {
            throw new BaseException(ERROR_MESSAGE);
        }
    }

    private Object handleBinance(JSONObject jsonObject) {
        try {
            return jsonObject.get("price");
        } catch (JSONException e) {
            throw new BaseException(ERROR_MESSAGE);
        }
    }

    private Object handleKraken(JSONObject jsonObject) {
        try {
            return ((JSONArray) ((JSONObject) ((JSONObject) jsonObject.get("result")).get("XXMRZUSD")).get("a")).get(0);
        } catch (JSONException e) {
            throw new BaseException(ERROR_MESSAGE);
        }
    }

    private Object handleBtcCoingecko(JSONObject jsonObject) {
        try {
            return ((JSONObject) jsonObject.get("bitcoin")).get("rub");
        } catch (JSONException e) {
            throw new BaseException(ERROR_MESSAGE);
        }
    }

    private Object handleLtcCoingecko(JSONObject jsonObject) {
        try {
            return ((JSONObject) jsonObject.get("litecoin")).get("rub");
        } catch (JSONException e) {
            throw new BaseException(ERROR_MESSAGE);
        }
    }

    private Object handleExchangerate(JSONObject jsonObject) {
        try {
            return ((JSONObject) jsonObject.get("conversion_rates")).get("RUB");
        } catch (JSONException e) {
            throw new BaseException(ERROR_MESSAGE);
        }
    }

    private BigDecimal parse(Object price) {
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

    private JSONObject readJsonFromUrl(String url) {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(2500);
        factory.setConnectionRequestTimeout(2500);
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
