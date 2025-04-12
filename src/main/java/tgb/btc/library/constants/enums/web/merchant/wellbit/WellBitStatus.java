package tgb.btc.library.constants.enums.web.merchant.wellbit;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum WellBitStatus {
    NEW("new", "Новый"),
    COMPLETE("complete", "Вывод выполнен"),
    CANCEL("cancel", "Вывод отменен"),
    CHARGE_BACK("chargeback", "По выводу произведён возврат средств");

    private final String value;

    private final String description;

    public static WellBitStatus fromValue(String value) {
        for (WellBitStatus wellBitStatus : WellBitStatus.values()) {
            if (wellBitStatus.getValue().equals(value)) {
                return wellBitStatus;
            }
        }
        return null;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class Deserializer extends JsonDeserializer<WellBitStatus> {

        @Override
        public WellBitStatus deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
            return WellBitStatus.fromValue(p.getValueAsString());
        }
    }
}
