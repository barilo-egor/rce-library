package tgb.btc.library.constants.enums.web.merchant.onlypays;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum OnlyPaysStatus {
    WAITING("waiting", "Ожидает оплаты"),
    CANCELLED("cancelled", "Отменен"),
    FINISHED("finished", "Завершен");

    private final String value;

    private final String description;

    public static OnlyPaysStatus fromValue(String value) {
        for (OnlyPaysStatus onlyPaysStatus : OnlyPaysStatus.values()) {
            if (onlyPaysStatus.getValue().equals(value)) {
                return onlyPaysStatus;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<OnlyPaysStatus> {

        @Override
        public OnlyPaysStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return OnlyPaysStatus.fromValue(p.getValueAsString());
        }
    }
}
