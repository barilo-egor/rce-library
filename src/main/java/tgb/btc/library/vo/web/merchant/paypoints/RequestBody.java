package tgb.btc.library.vo.web.merchant.paypoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import tgb.btc.library.constants.enums.bot.FiatCurrency;
import tgb.btc.library.constants.serialize.FiatCurrencyNameSerializer;

@Data
public class RequestBody {

    @JsonProperty("merchant_transaction_id")
    private String merchantTransactionId;

    private Integer amount;

    @JsonSerialize(using = FiatCurrencyNameSerializer.class)
    private FiatCurrency currency;
}
