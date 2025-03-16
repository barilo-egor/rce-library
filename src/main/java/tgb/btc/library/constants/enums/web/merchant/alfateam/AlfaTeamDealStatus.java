package tgb.btc.library.constants.enums.web.merchant.alfateam;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@AllArgsConstructor
@Getter
@Slf4j
public enum AlfaTeamDealStatus {
    NEW("new", "Сделка только что создана."),
    TRANSFER_WAITING("transfer_waiting", "Ожидается перевод средств."),
    TRANSFER_CONFIRMED("transfer_confirmed", "Перевод средств подтвержден пользователем."),
    CANCELED("canceled", "Сделка была отменена."),
    COMPLETED("completed", "Сделка успешно завершена."),
    DISPUTE("dispute", "Сделка находится в стадии спора.");

    private final String value;

    private final String description;

    public static AlfaTeamDealStatus fromValue(String value) {
        for (AlfaTeamDealStatus status : AlfaTeamDealStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<AlfaTeamDealStatus> {
        @Override
        public void serialize(AlfaTeamDealStatus alfaTeamDealStatus, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(alfaTeamDealStatus.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<AlfaTeamDealStatus> {
        @Override
        public AlfaTeamDealStatus deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return AlfaTeamDealStatus.fromValue(jsonParser.getValueAsString());
        }
    }
}
