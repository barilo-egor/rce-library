package tgb.btc.library.vo.web.merchant.crocopay;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.crocopay.CrocoPayStatus;

@Data
public class Transaction {

    private String id;

    @JsonDeserialize(using = CrocoPayStatus.Deserializer.class)
    private CrocoPayStatus status;
}
