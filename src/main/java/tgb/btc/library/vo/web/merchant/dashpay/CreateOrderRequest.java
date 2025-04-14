package tgb.btc.library.vo.web.merchant.dashpay;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderMethod;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private String id;

    private final String type = "deposit";

    @JsonSerialize(using = OrderMethod.Serializer.class)
    private OrderMethod method;

    private Double sum;

    private Customer customer;

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Customer {

        private String id;
    }
}
