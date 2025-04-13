package tgb.btc.library.vo.web.merchant.alfateam;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.alfateam.PaymentMethod;

@Data
public class DealDTO {

    private String id;

    @JsonDeserialize(using = PaymentMethod.Deserializer.class)
    private PaymentMethod paymentMethod;

    private RequisitesDTO requisites;
}
