package tgb.btc.library.constants.enums.web.merchant.payscrow;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    FAILED_TO_SERVICE("FailedToService"),
    UNPAID("Unpaid"),
    PAID("Paid"),
    CANCELED_BY_CUSTOMER("CanceledByCustomer"),
    CANCELED_BY_MERCHANT("CanceledByMerchant"),
    CANCELED_BY_TRADER("CanceledByTrader"),
    CANCELED_BY_TIMEOUT("CanceledByTimeout"),
    CANCELED_BY_ADMIN("CanceledByAdmin"),
    COMPLETED("Completed"),
    PROCESSING("Processing"),
    QUEUED("Queued"),;

    final String value;

    public static OrderStatus fromValue(String v) {
        for (OrderStatus c: OrderStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<OrderStatus> {
        @Override
        public OrderStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return OrderStatus.fromValue(jsonParser.getValueAsString());
        }
    }
}
