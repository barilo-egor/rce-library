package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import tgb.btc.library.constants.serialize.BigDecimalSerializer;

import java.math.BigDecimal;

@Data
@Builder
public class PayscrowOrderRequest {

    private String externalOrderId;

    private final String orderSide = "Buy";

    private String basePaymentMethodId;

    @JsonSerialize(using = BigDecimalSerializer.class)
    private BigDecimal targetAmount;

    private final String feeType = "ChargeMerchant";

    private final String currencyType = "Fiat";

    private String currency;
}
