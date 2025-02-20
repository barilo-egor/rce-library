package tgb.btc.library.constants.enums.web.merchant.payscrow;

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
public enum OrderSide {
    BUY("Buy"),
    SELL("Sell");

    final String value;

    public static OrderSide fromValue(String v) {
        for (OrderSide c : OrderSide.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<OrderSide> {
        @Override
        public void serialize(OrderSide orderSide, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(orderSide.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<OrderSide> {

        @Override
        public OrderSide deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return OrderSide.fromValue(jsonParser.getValueAsString());
        }
    }
}
