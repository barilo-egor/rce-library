package tgb.btc.library.constants.enums.web.merchant.alfateam;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@AllArgsConstructor
@Getter
public enum DirectionType {
    IN("in"),
    OUT("out");

    private final String value;

    public static DirectionType fromValue(String value) {
        for (DirectionType type : DirectionType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<DirectionType> {
        @Override
        public void serialize(DirectionType directionType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(directionType.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<DirectionType> {
        @Override
        public DirectionType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return DirectionType.fromValue(jsonParser.getValueAsString());
        }
    }
}
