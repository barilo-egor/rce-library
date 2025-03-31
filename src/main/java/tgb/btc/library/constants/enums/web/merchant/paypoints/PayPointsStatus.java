package tgb.btc.library.constants.enums.web.merchant.paypoints;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Getter
public enum PayPointsStatus {
    PAID("paid", "Оплачено"),
    UNDERPAID("underpaid", "Недоплачено"),
    OVERPAID("overpaid", "Переплачено"),
    PROCESS("process", "Ожидает оплаты"),
    EXPIRED("expired", "Просрочена"),
    CANCEL("cancel", "Отменена мерчантом"),
    ERROR("error", "Произошла ошибка при создании платежа");

    public static final List<PayPointsStatus> FINAL_STATUSES = List.of(PROCESS);

    private final String value;

    private final String displayName;

    public static PayPointsStatus fromValue(String value) {
        for (PayPointsStatus payPointsStatus : PayPointsStatus.values()) {
            if (payPointsStatus.getValue().equals(value)) {
                return payPointsStatus;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<PayPointsStatus> {

        @Override
        public PayPointsStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return PayPointsStatus.fromValue(p.getValueAsString());
        }
    }
}
