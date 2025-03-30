package tgb.btc.library.vo.web.merchant.paypoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionResponse extends PayPointsResponse {

    @JsonProperty()
    private Long id;

    @JsonProperty("merchant_transaction_id")
    private String merchantTransactionId;

    @JsonProperty("bank_name")
    private String bankName;
}
