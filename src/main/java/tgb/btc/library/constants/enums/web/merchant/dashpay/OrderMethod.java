package tgb.btc.library.constants.enums.web.merchant.dashpay;

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
public enum OrderMethod {
    PHONE_NUMBER("phone_number", "Номер телефона"),
    CARD_NUMBER("card_number", "Карта");

    private final String value;

    private final String displayName;

    public static OrderMethod getByValue(String value) {
        for (OrderMethod orderMethod : OrderMethod.values()) {
            if (orderMethod.getValue().equals(value)) {
                return orderMethod;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<OrderMethod> {
        @Override
        public void serialize(OrderMethod orderMethod, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(orderMethod.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<OrderMethod> {
        @Override
        public OrderMethod deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return OrderMethod.getByValue(jsonParser.getText());
        }
    }
}
