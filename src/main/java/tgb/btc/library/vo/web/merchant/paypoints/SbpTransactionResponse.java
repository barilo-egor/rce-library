package tgb.btc.library.vo.web.merchant.paypoints;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SbpTransactionResponse extends TransactionResponse{

    @JsonProperty("phone_number")
    private String phoneNumber;
}
