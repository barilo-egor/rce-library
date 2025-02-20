package tgb.btc.library.constants.enums.web.merchant.payscrow;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;

@Getter
@AllArgsConstructor
public enum FeeType {

    CHARGE_CUSTOMER("ChargeCustomer"),
    CHARGE_MERCHANT("ChargeMerchant"),;

    final String value;

    public static FeeType fromValue(String value) {
        for (FeeType type : FeeType.values()) {
            if (type.getValue().equals(value)) {
                return type;
            }
        }
        return null;
    }

    public static class Serializer extends JsonSerializer<FeeType> {

        @Override
        public void serialize(FeeType feeType, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(feeType.getValue());
        }
    }

    public static class Deserializer extends JsonDeserializer<FeeType> {

        @Override
        public FeeType deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return FeeType.fromValue(jsonParser.getValueAsString());
        }
    }
}
