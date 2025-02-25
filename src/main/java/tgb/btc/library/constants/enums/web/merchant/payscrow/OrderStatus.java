package tgb.btc.library.constants.enums.web.merchant.payscrow;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum OrderStatus {

    FAILED_TO_SERVICE("FailedToService", true, "Не удалось обслужить по тех.причинам"),
    UNPAID("Unpaid", false, "Не оплачено"),
    PAID("Paid", false, "Оплачено"),
    CANCELED_BY_CUSTOMER("CanceledByCustomer", true, "Отменено клиентом"),
    CANCELED_BY_MERCHANT("CanceledByMerchant", true, "Отменено мерчантом"),
    CANCELED_BY_TRADER("CanceledByTrader", true, "Отменено исполнителем"),
    CANCELED_BY_TIMEOUT("CanceledByTimeout", true, "Достигнут лимит времени"),
    CANCELED_BY_ADMIN("CanceledByAdmin", true, "Отменено администратором"),
    COMPLETED("Completed", true, "Успешно выполнен"),
    PROCESSING("Processing", false, "В процессе обработки исполнителем"),
    QUEUED("Queued", false, "Зарегестрирован без назначения исполнителя");

    public static final List<OrderStatus> STATUSES_TO_SEARCH = List.of(
            FAILED_TO_SERVICE, CANCELED_BY_CUSTOMER, CANCELED_BY_MERCHANT, CANCELED_BY_TRADER, CANCELED_BY_TIMEOUT,
            CANCELED_BY_ADMIN, COMPLETED, PROCESSING, QUEUED
    );

    final String value;

    final boolean isFinal;

    final String description;

    public static OrderStatus fromValue(String v) {
        for (OrderStatus c: OrderStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        return null;
    }

    public static List<OrderStatus> getNotFinal() {
        return Arrays.stream(OrderStatus.values()).filter(status -> !status.isFinal).toList();
    }

    public static class Deserializer extends JsonDeserializer<OrderStatus> {
        @Override
        public OrderStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return OrderStatus.fromValue(jsonParser.getValueAsString());
        }
    }
}
