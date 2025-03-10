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

@Getter
@AllArgsConstructor
public enum PaymentOption {
    SBP("SBP", "СБП"),
    TO_CARD("TO_CARD", "Перевод на карту."),
    CROSS_BORDER("CROSS_BORDER", "Трансграничный перевод");

    private final String value;

    private final String description;

    public static PaymentOption fromValue(String value) {
        for (PaymentOption e : PaymentOption.values()) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<PaymentOption> {
        @Override
        public void serialize(PaymentOption paymentOption, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(paymentOption.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<PaymentOption> {
        @Override
        public PaymentOption deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return PaymentOption.fromValue(jsonParser.getValueAsString());
        }
    }
}
