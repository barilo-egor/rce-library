package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.payscrow.CurrencyType;
import tgb.btc.library.constants.enums.web.merchant.payscrow.FeeType;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderSide;
import tgb.btc.library.constants.serialize.BigDecimalSerializer;

import java.math.BigDecimal;

@Data
@Builder
public class PayscrowOrderRequest {

    private String externalOrderId;

    @JsonSerialize(using = OrderSide.Serializer.class)
    private OrderSide orderSide;

    private String basePaymentMethodId;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal targetAmount;

    @JsonSerialize(using = FeeType.Serializer.class)
    private FeeType feeType;

    private CurrencyType currencyType;

    private String currency;

    private String customerName;

    private String customerPaymentAccount;

    private String balanceCurrency;
}
