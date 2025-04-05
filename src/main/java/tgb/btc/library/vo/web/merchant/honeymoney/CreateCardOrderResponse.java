package tgb.btc.library.vo.web.merchant.honeymoney;

import lombok.Data;

@Data
public class CreateCardOrderResponse {

    private Integer id;

    private String cardNumber;

    private String bankName;
}
