package tgb.btc.library.vo.web.merchant.dashpay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Confirmation confirmation;

    private Data data;

    @lombok.Data
    public static class Data {
        private Order order;
    }
}
