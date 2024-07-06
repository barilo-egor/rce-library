package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.exception.EnumTypeNotFoundException;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;

public enum CryptoCurrency implements ObjectNodeConvertable<CryptoCurrency> {
    BITCOIN("btc", 8, 0.004),
    LITECOIN("ltc", 8, 0.7),
    USDT("usdt", 6, 50.0),
    MONERO("xmr", 8, 0.5);

    final String shortName;
    final int scale;
    final Double defaultCheckValue;

    CryptoCurrency(String shortName, int scale, Double defaultCheckValue) {
        this.shortName = shortName;
        this.scale = scale;
        this.defaultCheckValue = defaultCheckValue;
    }

    public Double getDefaultCheckValue() {
        return defaultCheckValue;
    }

    public int getScale() {
        return scale;
    }

    public String getShortName() {
        return shortName;
    }

    public static CryptoCurrency fromShortName(String shortName) {
        return Arrays.stream(CryptoCurrency.values()).filter(t -> t.getShortName().equals(shortName)).findFirst()
                .orElseThrow(() -> new EnumTypeNotFoundException("Не найдена крипто валюта: " + shortName));
    }
    @Override
    public Function<CryptoCurrency, ObjectNode> mapFunction() {
        return cryptoCurrency -> JacksonUtil.getEmpty()
                .put("name", cryptoCurrency.name())
                .put("shortName", cryptoCurrency.getShortName());
    }

    public static CryptoCurrency valueOfNullable(String name) {
        if (Objects.isNull(name)) return null;
        CryptoCurrency cryptoCurrency;
        try {
            cryptoCurrency = CryptoCurrency.valueOf(name);
        } catch (IllegalArgumentException e) {
            cryptoCurrency = null;
        }
        return cryptoCurrency;
    }
}
