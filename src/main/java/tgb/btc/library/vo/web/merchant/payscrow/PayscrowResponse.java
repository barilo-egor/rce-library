package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.payscrow.ErrorCode;

@Data
public class PayscrowResponse {

    private Boolean success;

    private String message;

    @JsonDeserialize(using = ErrorCode.Deserializer.class)
    private ErrorCode errorCode;
}
