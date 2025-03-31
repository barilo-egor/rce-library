package tgb.btc.library.constants.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

import java.io.IOException;

public class FiatCurrencyNameSerializer extends JsonSerializer<FiatCurrency> {

    @Override
    public void serialize(FiatCurrency value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.name());
    }
}
