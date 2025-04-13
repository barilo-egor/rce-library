package tgb.btc.library.vo.web.merchant.payscrow;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PayscrowOrderResponse extends PayscrowResponse {

    private Integer orderId;

    private String paymentMethodType;

    private String currency;

    private String holderAccount;

    private String methodName;
}
