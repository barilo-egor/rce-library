package tgb.btc.library.vo.web.merchant.wellbit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateOrderRequest {

    private final String currency = "RUB";

    private Integer amount;

    @JsonProperty("credential_type")
    private String credentialType;

    @JsonProperty("bank_code")
    private String bankCode;

    @JsonProperty("credential_require")
    private final String credentialRequire = "yes";

    @JsonProperty("custom_number")
    private String customNumber;

    @JsonProperty("client_ip")
    private String clientIp;

    @JsonProperty("client_email")
    private String email;
}
