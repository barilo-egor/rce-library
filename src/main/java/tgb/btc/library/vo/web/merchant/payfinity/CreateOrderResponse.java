package tgb.btc.library.vo.web.merchant.payfinity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CreateOrderResponse extends Response<CreateOrderResponse.Data> {

    @lombok.Data
    public static class Data {
        private String trackerID;

        private String cardNumber;

        @JsonProperty("SBPPhoneNumber")
        private String SBPPhoneNumber;

        private String bank;
    }
}
