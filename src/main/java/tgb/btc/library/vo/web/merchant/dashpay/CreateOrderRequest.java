package tgb.btc.library.vo.web.merchant.dashpay;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderMethod;
import tgb.btc.library.constants.enums.web.merchant.dashpay.OrderType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateOrderRequest {

    private String id;

    @JsonSerialize(using = OrderType.Serializer.class)
    private OrderType type;

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
