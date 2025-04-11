package tgb.btc.library.vo.web.merchant.honeymoney;

import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.honeymoney.HoneyMoneyStatus;

@Data
public class TransactionCallback {

    private String orderId;

    private HoneyMoneyStatus status;
}
