package tgb.btc.library.constants.enums.web.merchant.alfateam;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum PaymentMethod {
    TINKOFF("tinkoff", "Т-банк"),
    SBER_BANK("sberbank", "Сбербанк"),
    ALFA_BANK("alfabank", "Альфа-Банк");

    private final String value;

    private final String displayName;

    public static PaymentMethod fromValue(String value) {
        for (PaymentMethod paymentMethod : PaymentMethod.values()) {
            if (paymentMethod.value.equals(value)) {
                return paymentMethod;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<PaymentMethod> {
        @Override
        public void serialize(PaymentMethod paymentMethod, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(paymentMethod.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<PaymentMethod> {
        @Override
        public PaymentMethod deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return PaymentMethod.fromValue(jsonParser.getValueAsString());
        }
    }
}
