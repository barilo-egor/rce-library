package tgb.btc.library.constants.enums.web.merchant.crocopay;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum CrocoPayStatus {
    PENDING("Ожидание"),
    CANCEL("Отменен"),
    PAID("Оплачен");

    private final String description;

    public static CrocoPayStatus fromValue(String value) {
        for (CrocoPayStatus status : CrocoPayStatus.values()) {
            if (status.name().equals(value.toUpperCase())) {
                return status;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<CrocoPayStatus> {

        @Override
        public CrocoPayStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return CrocoPayStatus.fromValue(p.getValueAsString());
        }
    }
}
