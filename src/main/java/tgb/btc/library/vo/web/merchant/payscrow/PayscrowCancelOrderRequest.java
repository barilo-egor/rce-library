package tgb.btc.library.vo.web.merchant.payscrow;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PayscrowCancelOrderRequest {

    private Integer orderId;

    private Boolean requestedByCustomer;
}
