package tgb.btc.library.constants.enums.web.merchant.dashpay;

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
public enum DashPayOrderStatus {
    NEW("new", "Ожидает обработки"),
    EMPTY_TRADER("empty_trader", "Трейдер не найден"),
    CONFIRMED("confirmed", "Финализирован успехом"),
    RECALCULATED_PROCESSING("recalculated_processing", "Ожидает подтверждения комиссии"),
    RECALCULATED_CONFIRMED("recalculated_confirmed", "Финализирован с перерасчётом"),
    CANCELED("canceled", "Отменен"),
    EXPIRED("expired", "Время на оплату истекло");

    private final String code;

    private final String description;

    public static final List<DashPayOrderStatus> FINAL_STATUSES = List.of(NEW, RECALCULATED_PROCESSING);

    public static DashPayOrderStatus fromCode(String code) {
        for (DashPayOrderStatus status : DashPayOrderStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<DashPayOrderStatus> {
        @Override
        public DashPayOrderStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return DashPayOrderStatus.fromCode(jsonParser.getValueAsString());
        }
    }
}
