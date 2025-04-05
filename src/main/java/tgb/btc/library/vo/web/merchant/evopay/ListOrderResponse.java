package tgb.btc.library.vo.web.merchant.evopay;

import lombok.Data;

import java.util.List;

@Data
public class ListOrderResponse {

    private List<OrderResponse> entries;
}
