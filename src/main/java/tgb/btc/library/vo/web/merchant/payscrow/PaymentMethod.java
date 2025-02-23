package tgb.btc.library.vo.web.merchant.payscrow;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.payscrow.BankCard;

@Data
public class PaymentMethod {

    private String methodId;

    @JsonDeserialize(using = BankCard.Deserializer.class)
    private BankCard bankCard;

    private String name;

    private String fiatName;

    private Boolean available;

    private Long nspkCode;
}
