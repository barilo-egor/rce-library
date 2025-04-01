package tgb.btc.library.vo.web.merchant.onlypays;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysStatus;

@Data
public class GetStatusResponse {

    private boolean success;

    private String error;

    private Data data;

    @lombok.Data
    public static class Data {

        private String id;

        @JsonDeserialize(using = OnlyPaysStatus.Deserializer.class)
        private OnlyPaysStatus status;
    }
}
