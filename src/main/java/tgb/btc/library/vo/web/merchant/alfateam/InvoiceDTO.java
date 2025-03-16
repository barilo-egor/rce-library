package tgb.btc.library.vo.web.merchant.alfateam;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.ToString;
import tgb.btc.library.constants.enums.web.merchant.alfateam.InvoiceStatus;

@Data
@ToString
public class InvoiceDTO {

    private String id;

    @JsonDeserialize(using = InvoiceStatus.Deserializer.class)
    private InvoiceStatus status;
}
