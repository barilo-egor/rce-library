package tgb.btc.library.vo.web.merchant.paypoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardTransactionResponse extends TransactionResponse {

    @JsonProperty("card_number")
    private String cardNumber;
}
