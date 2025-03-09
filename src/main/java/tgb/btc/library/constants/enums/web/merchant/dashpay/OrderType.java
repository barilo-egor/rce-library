package tgb.btc.library.constants.enums.web.merchant.dashpay;

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
public enum OrderType {
    DEPOSIT("deposit"),
    CASH_OUT("cash_out");

    private final String value;

    public static OrderType getByValue(String value) {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.getValue().equals(value)) {
                return orderType;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<OrderType> {

        @Override
        public void serialize(OrderType orderType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(orderType.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<OrderType> {

        @Override
        public OrderType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return OrderType.getByValue(jsonParser.getValueAsString());
        }
    }
}
