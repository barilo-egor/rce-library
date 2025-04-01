package tgb.btc.library.vo.web.merchant.onlypays;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetStatusRequest {

    @JsonProperty("api_id")
    private String apiId;

    @JsonProperty("secret_key")
    private String secretKey;

    private String id;
}
