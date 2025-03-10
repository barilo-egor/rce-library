package tgb.btc.library.vo.web.merchant.alfateam;

import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.alfateam.AlfaTeamDealStatus;

@Data
public class InvoiceDTO {

    private String id;

    private AlfaTeamDealStatus status;
}
