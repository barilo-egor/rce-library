package tgb.btc.library.vo.web.merchant.paypoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionResponse extends PayPointsResponse {

    @JsonProperty
    private Long id;

    @JsonProperty("merchant_transaction_id")
    private String merchantTransactionId;

    @JsonProperty("bank_name")
    private String bankName;

    @JsonDeserialize(using = PayPointsStatus.Deserializer.class)
    private PayPointsStatus status;
}
