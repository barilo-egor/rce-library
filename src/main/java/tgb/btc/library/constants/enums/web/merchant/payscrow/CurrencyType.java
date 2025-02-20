package tgb.btc.library.constants.enums.web.merchant.payscrow;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum CurrencyType {
    FIAT("Fiat"),
    CRYPTO("Crypto");

    final String value;

    public static CurrencyType fromValue(String value) {
        for (CurrencyType currencyType : CurrencyType.values()) {
            if (currencyType.value.equals(value)) {
                return currencyType;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<CurrencyType> {

        @Override
        public CurrencyType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return CurrencyType.fromValue(jsonParser.getValueAsString());
        }
    }
}
