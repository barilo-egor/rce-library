package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.payscrow.BankCard;

@Data
public class PaymentMethodsFilter {

    private String fiatName;

    @JsonSerialize(using = BankCard.Serializer.class)
    private BankCard type;

    private Boolean availableOnly;
}
