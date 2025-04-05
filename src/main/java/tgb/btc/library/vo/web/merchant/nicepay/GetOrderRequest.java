package tgb.btc.library.vo.web.merchant.nicepay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetOrderRequest {

    @JsonProperty("merchant_id")
    private String merchantId;

    private String secret;

    private String payment;
}
