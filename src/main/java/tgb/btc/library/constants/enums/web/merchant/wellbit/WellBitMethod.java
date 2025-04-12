package tgb.btc.library.constants.enums.web.merchant.wellbit;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum WellBitMethod {
    SBP("sbp", "СБП"),
    CARDS("cards", "Карта");

    private final String value;

    private final String description;

    public static class Serializer extends JsonSerializer<WellBitMethod> {

        @Override
        public void serialize(WellBitMethod value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getValue());
        }
    }
}
