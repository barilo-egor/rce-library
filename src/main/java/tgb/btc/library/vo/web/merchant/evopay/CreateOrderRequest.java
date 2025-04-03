package tgb.btc.library.vo.web.merchant.evopay;

import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayPaymentMethod;

@Data
public class CreateOrderRequest {

    private String customId;

    private EvoPayPaymentMethod paymentMethod;

    private Integer fiatSum;

    private final String fiatCurrencyCode = "RUB";

    private final String cryptoCurrencyCode = "USDT";
}
