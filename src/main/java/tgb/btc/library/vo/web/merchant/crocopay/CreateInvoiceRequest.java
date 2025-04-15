package tgb.btc.library.vo.web.merchant.crocopay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.crocopay.CrocoPayMethod;

@Data
public class CreateInvoiceRequest {

    private Integer amount;

    private final String currency = "RUB";

    @JsonProperty("payment_option")
    private CrocoPayMethod crocoPayMethod;
}
