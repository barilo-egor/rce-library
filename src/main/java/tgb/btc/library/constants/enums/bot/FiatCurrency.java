package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.exception.EnumTypeNotFoundException;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.Objects;
import java.util.function.Function;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FiatCurrency implements ObjectNodeConvertable<FiatCurrency> {
    /**
     * Бел.рубль
     */
    BYN("byn", "Бел.рубли", "бел.рублей", "\uD83C\uDDE7\uD83C\uDDFE"),
    /**
     * Рос.рубль
     */
    RUB("rub", "Рос.рубли", "₽", "\uD83C\uDDF7\uD83C\uDDFA"),
    UAH("uah", "Гривны", "гривен", "\uD83C\uDDFA\uD83C\uDDE6");

    final String code;

    final String displayName;

    final String genitive;

    final String flag;

    FiatCurrency(String code, String displayName, String genitive, String flag) {
        this.code = code;
        this.displayName = displayName;
        this.genitive = genitive;
        this.flag = flag;
    }

    public String getName() {
        return this.name();
    }

    public String getGenitive() {
        return genitive;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

    public String getFlag() {
        return flag;
    }

    public static FiatCurrency getByCode(String code) {
        for (FiatCurrency fiatCurrency : FiatCurrency.values()) {
            if (fiatCurrency.getCode().equals(code)) return fiatCurrency;
        }
        throw new EnumTypeNotFoundException("Фиатная валюта не найдена.");
    }

    @Override
    public Function<FiatCurrency, ObjectNode> mapFunction() {
        return fiatCurrency -> JacksonUtil.getEmpty()
                .put("name", fiatCurrency.name())
                .put("code", fiatCurrency.getCode())
                .put("displayName", fiatCurrency.getDisplayName())
                .put("genitive", fiatCurrency.getGenitive())
                .put("flag", fiatCurrency.getFlag());
    }

    public static FiatCurrency valueOfNullable(String name) {
        if (Objects.isNull(name)) return null;
        FiatCurrency fiatCurrency;
        try {
            fiatCurrency = FiatCurrency.valueOf(name);
        } catch (IllegalArgumentException e) {
            fiatCurrency = null;
        }
        return fiatCurrency;
    }
}
