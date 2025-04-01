package tgb.btc.library.vo.web.merchant.onlypays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysPaymentType;

@Data
public class GetRequisiteResponse {

    private boolean success;

    private String error;

    private Data data;

    @lombok.Data
    public static class Data {

        private String id;

        private String requisite;

        private String owner;

        private String bank;

        @JsonProperty("payment_type")
        @JsonDeserialize(using = OnlyPaysPaymentType.Deserializer.class)
        private OnlyPaysPaymentType paymentType;
    }
}
