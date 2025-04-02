package tgb.btc.library.constants.enums.web.merchant.onlypays;

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
public enum OnlyPaysPaymentType {
    CARD("card", "Карта"),
    SBP("sbp", "СБП");

    private final String value;

    private final String description;

    public static OnlyPaysPaymentType fromValue(String value) {
        for (OnlyPaysPaymentType onlyPaysPaymentType : OnlyPaysPaymentType.values()) {
            if (onlyPaysPaymentType.getValue().equals(value)) {
                return onlyPaysPaymentType;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<OnlyPaysPaymentType> {
        @Override
        public void serialize(OnlyPaysPaymentType value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<OnlyPaysPaymentType> {

        @Override
        public OnlyPaysPaymentType deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return OnlyPaysPaymentType.fromValue(p.getValueAsString());
        }
    }
}
