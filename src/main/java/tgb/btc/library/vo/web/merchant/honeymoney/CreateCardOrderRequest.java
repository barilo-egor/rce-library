package tgb.btc.library.vo.web.merchant.honeymoney;

import lombok.Data;

@Data
public class CreateCardOrderRequest {

    private String extId;

    private final String currency = "RUB";

    private Integer amount;

    private String bank;

    private ClientDetails clientDetails;

    @Data
    public static class ClientDetails {

        private String clientId;
    }
}
