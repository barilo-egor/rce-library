package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;

@Data
public class Order {

    private Integer orderId;

    @JsonDeserialize(using = OrderStatus.Deserializer.class)
    private OrderStatus orderStatus;
}
