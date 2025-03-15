package tgb.btc.library.vo.web.merchant.dashpay;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import tgb.btc.library.constants.serialize.LocalDateTimeSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersRequest {

    @JsonProperty("filter[createdAtFrom]")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAtFrom;

    @JsonProperty("filter[createdAtTo]")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAtTo;

    @JsonProperty("pagination[perPage]")
    private Integer perPage;

    @JsonProperty("pagination[currentPage]")
    private Integer currentPage;

    public String buildParams() {
        StringBuilder params = new StringBuilder();
        params.append("?");
        if (Objects.nonNull(createdAtFrom)) {
            params.append("filter[createdAtFrom]=").append(createdAtFrom.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .append("&");
        }
        if (Objects.nonNull(createdAtTo)) {
            params.append("filter[createdAtTo]=").append(createdAtTo.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .append("&");
        }
        if (Objects.nonNull(perPage)) {
            params.append("filter[perPage]=").append(perPage).append("&");
        }
        if (Objects.nonNull(currentPage)) {
            params.append("filter[currentPage]=").append(currentPage).append("&");
        }
        if (params.length() == 1) {
            return "";
        } else {
            return params.deleteCharAt(params.length() - 1).toString();
        }
    }
}
