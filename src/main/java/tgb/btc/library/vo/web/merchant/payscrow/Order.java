package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.payscrow.*;
import tgb.btc.library.constants.serialize.BigDecimalDeserializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Order {

    private Integer orderId;

    private String externalOrderId;

    @JsonDeserialize(using = OrderSide.Deserializer.class)
    private OrderSide orderSide;

    private String basePaymentMethodId;

    @JsonDeserialize(using = PaymentMethodType.Deserializer.class)
    private PaymentMethodType paymentMethodType;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal targetAmount;

    @JsonDeserialize(using = FeeType.Deserializer.class)
    private FeeType feeType;

    @JsonDeserialize(using = CurrencyType.Deserializer.class)
    private CurrencyType currencyType;

    private Currency currency;

    private String holderName;

    private String holderAccount;

    private BigDecimal fiatOrderAmount;

    private BigDecimal cryptoOrderAmount;

    @JsonDeserialize(using = OrderStatus.Deserializer.class)
    private OrderStatus orderStatus;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updated;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime unpaidOrderExpirationDate;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal referencePrice;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal margin;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal fiatFee;

    @JsonDeserialize(using = BigDecimalDeserializer.class)
    private BigDecimal cryptoFee;

    private Long nspkCode;

    @JsonDeserialize(using = CancelReason.Deserializer.class)
    private CancelReason cancelReason;

    private String cancelOrderReason;

    private String methodName;

    private String methodCountryCode;
}
