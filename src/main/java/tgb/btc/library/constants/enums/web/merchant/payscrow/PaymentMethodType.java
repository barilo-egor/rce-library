package tgb.btc.library.constants.enums.web.merchant.payscrow;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum PaymentMethodType {
    BANK_CARD("BankCard"),
    SBP("SBP");

    final String value;

    public static PaymentMethodType fromValue(String value) {
        for (PaymentMethodType type : PaymentMethodType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<PaymentMethodType> {
        @Override
        public PaymentMethodType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return PaymentMethodType.fromValue(jsonParser.getValueAsString());
        }
    }
}
