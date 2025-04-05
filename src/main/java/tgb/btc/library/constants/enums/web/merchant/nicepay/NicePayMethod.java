package tgb.btc.library.constants.enums.web.merchant.nicepay;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum NicePayMethod {
    SBP("sbp_rub", "СБП");

    private final String value;

    private final String description;

    public static NicePayMethod fromValue(String value) {
        for (NicePayMethod method : NicePayMethod.values()) {
            if (method.getValue().equals(value)) {
                return method;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<NicePayMethod> {

        @Override
        public void serialize(NicePayMethod value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(value.getValue());
        }
    }
}
