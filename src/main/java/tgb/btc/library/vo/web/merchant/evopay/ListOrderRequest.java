package tgb.btc.library.vo.web.merchant.evopay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;

import java.util.List;

@Data
public class ListOrderRequest {

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty
    private List<EvoPayStatus> statuses;

    private Integer page;

    private Integer limit;
}
