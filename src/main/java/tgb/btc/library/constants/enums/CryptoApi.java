package tgb.btc.library.constants.enums;

import lombok.extern.slf4j.Slf4j;
import tgb.btc.library.constants.enums.bot.CryptoCurrency;
import tgb.btc.library.exception.BaseException;

import java.util.List;

@Slf4j
public enum CryptoApi {
    BTC_USD_BLOCKCHAIN("https://api.blockchain.com/v3/exchange/tickers/BTC-USD"),
    BTC_USD_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=BTCUSDT"),
    LTC_USD_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=LTCUSDT"),
    LTC_USD_BLOCKCHAIN("https://api.blockchain.com/v3/exchange/tickers/LTC-USD"),
    XMR_USD_BINANCE("https://api.binance.com/api/v3/ticker/price?symbol=XMRUSDT"),
    XMR_USD_KRAKEN("https://api.kraken.com/0/public/Ticker?pair=XMRUSD"),
    BTC_RUB_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=BTCRUB"),
    LTC_RUB_BINANCE("https://api1.binance.com/api/v3/avgPrice?symbol=LTCRUB"),
    BTC_RUB_COINGECKO("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=rub"),
    LTC_RUB_COINGECKO("https://api.coingecko.com/api/v3/simple/price?ids=litecoin&vs_currencies=rub"),
    USD_RUB_EXCHANGERATE("https://v6.exchangerate-api.com/v6/8ae628548cbe656cdc6f0a9e/latest/USD");

    final String url;

    CryptoApi(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public static final List<CryptoApi> BTC_USD = List.of(CryptoApi.BTC_USD_BINANCE, CryptoApi.BTC_USD_BLOCKCHAIN);

    public static final List<CryptoApi> LTS_USD = List.of(CryptoApi.LTC_USD_BINANCE);

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
}
