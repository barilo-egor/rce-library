package tgb.btc.library.vo.web.merchant.nicepay;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayStatus;

@Data
public class GetOrderResponse {

    private String status;

    @lombok.Data
    public static class Data {

        @JsonDeserialize(using = NicePayStatus.Deserializer.class)
        private NicePayStatus status;
    }
}
