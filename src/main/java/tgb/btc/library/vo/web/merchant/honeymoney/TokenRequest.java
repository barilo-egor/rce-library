package tgb.btc.library.vo.web.merchant.honeymoney;

import lombok.Data;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Data
public class TokenRequest {

    private String clientId;

    private String clientSecret;

    public MultiValueMap<String, String> toFormData() {
        MultiValueMap<String, String> result = new LinkedMultiValueMap<>();
        result.add("client_id", clientId);
        result.add("client_secret", clientSecret);
        result.add("grant_type", "client_credentials");
        return result;
    }
}
