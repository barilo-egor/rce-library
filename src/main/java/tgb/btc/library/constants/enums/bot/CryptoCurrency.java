package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.constants.enums.system.BotProperties;
import tgb.btc.library.exception.EnumTypeNotFoundException;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.Arrays;
import java.util.function.Function;

public enum CryptoCurrency implements ObjectNodeConvertable<CryptoCurrency> {
    BITCOIN(BotProperties.CRYPTO_CURRENCIES_DESIGN.getString("BITCOIN"), "btc", String.class, 8, 0.004),
    LITECOIN(BotProperties.CRYPTO_CURRENCIES_DESIGN.getString("LITECOIN"), "ltc", String.class, 8, 0.7),
    USDT(BotProperties.CRYPTO_CURRENCIES_DESIGN.getString("USDT"), "usdt", String.class, 1, 50.0),
    MONERO(BotProperties.CRYPTO_CURRENCIES_DESIGN.getString("MONERO"), "xmr", Double.class, 8, 0.5);

    final String displayName;
    final String shortName;
    final Class rateClass;
    final int scale;
    final Double defaultCheckValue;

    CryptoCurrency(String displayName, String shortName, Class rateClass, int scale, Double defaultCheckValue) {
        this.displayName = displayName;
        this.shortName = shortName;
        this.rateClass = rateClass;
        this.scale = scale;
        this.defaultCheckValue = defaultCheckValue;
    }

    public Double getDefaultCheckValue() {
        return defaultCheckValue;
    }

    public int getScale() {
        return scale;
    }

    public Class getRateClass() {
        return rateClass;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getShortName() {
        return shortName;
    }

    public static CryptoCurrency fromShortName(String shortName) {
        return Arrays.stream(CryptoCurrency.values()).filter(t -> t.getShortName().equals(shortName)).findFirst()
                .orElseThrow(() -> new EnumTypeNotFoundException("Не найдена крипто валюта: " + shortName));
    }

    public static CryptoCurrency fromDisplayName(String displayName) {
        return Arrays.stream(CryptoCurrency.values()).filter(t -> t.getDisplayName().equals(displayName)).findFirst()
                .orElseThrow(() -> new EnumTypeNotFoundException("Не найдена крипто валюта: " + displayName));
    }

    @Override
    public Function<CryptoCurrency, ObjectNode> mapFunction() {
        return cryptoCurrency -> JacksonUtil.getEmpty()
                .put("name", cryptoCurrency.name())
                .put("displayName", cryptoCurrency.getDisplayName());
    }
}
