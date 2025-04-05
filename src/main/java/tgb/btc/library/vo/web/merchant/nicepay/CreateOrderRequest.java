package tgb.btc.library.vo.web.merchant.nicepay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayMethod;

@Data
public class CreateOrderRequest {

    @JsonProperty("merchant_id")
    private String merchantId;

    @JsonProperty("secret")
    private String secret;

    @JsonProperty("order_id")
    private String orderId;

    private String customer;

    private Integer amount;

    private final String currency = "RUB";

    @JsonSerialize(using = NicePayMethod.Serializer.class)
    private NicePayMethod method;
}
