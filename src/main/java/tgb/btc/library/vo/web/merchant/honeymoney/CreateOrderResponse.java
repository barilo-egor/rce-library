package tgb.btc.library.vo.web.merchant.honeymoney;

import lombok.Data;

@Data
public class CreateOrderResponse {

    private Integer id;

    private String cardNumber;

    private String bankName;

    private String phoneNumber;
}
