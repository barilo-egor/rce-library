package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.EqualsAndHashCode;
import tgb.btc.library.constants.enums.web.merchant.payscrow.PaymentMethodType;

@EqualsAndHashCode(callSuper = true)
@Data
public class PayscrowOrderResponse extends PayscrowResponse {

    private Integer orderId;

    @JsonDeserialize(using = PaymentMethodType.Deserializer.class)
    private PaymentMethodType paymentMethodType;

    private String currency;

    private String holderAccount;

    private String methodName;
}
