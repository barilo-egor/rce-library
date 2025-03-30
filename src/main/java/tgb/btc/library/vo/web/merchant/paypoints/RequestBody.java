package tgb.btc.library.vo.web.merchant.paypoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tgb.btc.library.constants.enums.bot.FiatCurrency;

@Data
public class RequestBody {

    @JsonProperty("merchant_transaction_id")
    private String merchantTransactionId;

    private Integer amount;

    private FiatCurrency currency;
}
