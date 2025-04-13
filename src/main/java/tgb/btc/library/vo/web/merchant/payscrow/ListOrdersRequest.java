package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;
import tgb.btc.library.constants.serialize.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ListOrdersRequest {

    private final String orderSide = "Buy";

    @JsonSerialize(contentUsing = OrderStatus.Serializer.class)
    private List<OrderStatus> orderStatuses;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime from;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime to;
}
