package tgb.btc.library.vo.web.merchant.evopay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;

@Data
public class OrderResponse {

    private String id;

    private EvoPayStatus orderStatus;

    private Requisites requisites;

    @Data
    public static class Requisites {

        @JsonProperty("recipient_phone_number")
        private String recipientPhoneNumber;

        @JsonProperty("recipient_card_number")
        private String recipientCardNumber;

        @JsonProperty("recipient_bank")
        private String recipientBank;
    }
}
