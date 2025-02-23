package tgb.btc.library.constants.enums.web.merchant.payscrow;

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
public enum BankCard {
    BANK_CARD("BankCard"),
    SBP("SBP"),
    BANK_ACCOUNT("BankAccount"),
    CASH("Cash"),
    E_CURRENCY("ECurrency");

    final String value;

    public static BankCard getByValue(final String value) {
        for (BankCard bankCard : BankCard.values()) {
            if (bankCard.value.equals(value)) {
                return bankCard;
            }
        }
        return null;
    }

    public static class Deserializer extends JsonDeserializer<BankCard> {

        @Override
        public BankCard deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            return BankCard.getByValue(jsonParser.getValueAsString());
        }
    }

    public static class Serializer extends JsonSerializer<BankCard> {
        @Override
        public void serialize(BankCard bankCard, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString(bankCard.getValue());
        }
    }
}
