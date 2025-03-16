package tgb.btc.library.constants.enums.web.merchant.alfateam;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum InvoiceStatus {
    NEW("new", "Сделка только что создана."),
    TRANSFER_WAITING("transfer_waiting", "Ожидается перевод средств."),
    TRANSFER_CONFIRMED("transfer_confirmed", "Перевод средств подтвержден пользователем."),
    CANCELED("canceled", "Сделка была отменена."),
    COMPLETED("completed", "Сделка успешно завершена."),
    DISPUTE("dispute", "Сделка находится в стадии спора.");

    private final String value;

    private final String description;

    public static InvoiceStatus getByValue(String value) {
        for (InvoiceStatus status : InvoiceStatus.values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<InvoiceStatus> {
        @Override
        public InvoiceStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return InvoiceStatus.getByValue(jsonParser.getValueAsString());
        }
    }
}
