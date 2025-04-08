package tgb.btc.library.constants.enums.web.merchant.payfinity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum PayFinityStatus {
    PENDING("В ожидании"),
    SUCCESS("Успешно"),
    ERROR("Ошибка");

    final String description;

    public static class Deserializer extends JsonDeserializer<PayFinityStatus> {

        @Override
        public PayFinityStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return PayFinityStatus.valueOf(p.getValueAsString());
        }
    }
}
