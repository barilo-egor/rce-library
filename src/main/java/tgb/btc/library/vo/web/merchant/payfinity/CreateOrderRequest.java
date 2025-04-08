package tgb.btc.library.vo.web.merchant.payfinity;

import lombok.Data;

@Data
public class CreateOrderRequest {

    private String amount;

    private String bank;

    private String callbackUrl;

    private String clientId;

    private String currency;

    private String type;

}
