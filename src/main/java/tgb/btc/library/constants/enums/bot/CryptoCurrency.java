package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.exception.EnumTypeNotFoundException;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public enum CryptoCurrency implements ObjectNodeConvertable<CryptoCurrency> {
    BITCOIN("btc", 8, 0.004,
            "Биткоин отправлен ✅\nhttps://blockchair.com/bitcoin/address/%s",
            "https://blockchair.com/bitcoin/address/%s",
            "https://blockchair.com/bitcoin/transaction/%s",
            "Биткоин отправлен ✅"),
    LITECOIN("ltc", 8, 0.7,
            "Валюта отправлена.\nhttps://blockchair.com/ru/litecoin/address/%s",
            "https://blockchair.com/ru/litecoin/address/%s",
            "https://blockchair.com/litecoin/transaction/%s",
            "Валюта отправлена."),
    USDT("usdt", 6, 50.0,
            "Валюта отправлена.https://tronscan.io/#/address/%s",
            "", "", "Валюта отправлена."),
    MONERO("xmr", 8, 0.5,
            "Валюта отправлена.",
            "", "", "Валюта отправлена.");

    final String shortName;
    final int scale;
    final Double defaultCheckValue;
    final String sendMessage;
    final String hashUrl;
    final String addressUrl;
    final String message;

    CryptoCurrency(String shortName, int scale, Double defaultCheckValue, String sendMessage, String hashUrl, String addressUrl,
                   String message) {
        this.shortName = shortName;
        this.scale = scale;
        this.defaultCheckValue = defaultCheckValue;
        this.sendMessage = sendMessage;
        this.hashUrl = hashUrl;
        this.addressUrl = addressUrl;
        this.message = message;
    }

    public static final List<CryptoCurrency> ELECTRUM_CURRENCIES = List.of(CryptoCurrency.BITCOIN, CryptoCurrency.LITECOIN);

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

    public String getSendMessage() {
        return sendMessage;
    }

    public String getHashUrl() {
        return hashUrl;
    }

    public String getMessage() {
        return message;
    }

    public String getAddressUrl() {
        return addressUrl;
    }
}