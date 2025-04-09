package tgb.btc.library.vo.web.merchant.honeymoney;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private String extId;

    private final String currency = "RUB";

    private Integer amount;

    private String callbackUrl;

    private String bank;

    private ClientDetails clientDetails;

    @Data
    public static class ClientDetails {

        private String clientId;
    }
}
