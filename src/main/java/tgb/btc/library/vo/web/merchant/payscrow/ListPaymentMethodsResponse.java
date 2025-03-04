package tgb.btc.library.vo.web.merchant.payscrow;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ListPaymentMethodsResponse extends PayscrowResponse{

    private List<PaymentMethod> paymentMethods;
}
