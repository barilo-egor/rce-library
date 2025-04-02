package tgb.btc.library.vo.web.merchant.onlypays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysPaymentType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetRequisiteRequest {

    @JsonProperty("api_id")
    private String apiId;

    @JsonProperty("secret_key")
    private String secretKey;

    @JsonProperty("amount_rub")
    private Integer amount;

    @JsonProperty("payment_type")
    @JsonSerialize(using = OnlyPaysPaymentType.Serializer.class)
    private OnlyPaysPaymentType paymentType;

    @JsonProperty("personal_id")
    private String personalId;
}
