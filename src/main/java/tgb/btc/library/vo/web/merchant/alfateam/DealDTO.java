package tgb.btc.library.vo.web.merchant.alfateam;

import lombok.Data;
import tgb.btc.library.constants.enums.web.merchant.alfateam.AlfaTeamDealStatus;
import tgb.btc.library.constants.enums.web.merchant.alfateam.PaymentMethod;

@Data
public class DealDTO {

    private String id;

    private AlfaTeamDealStatus status;

    private PaymentMethod paymentMethod;

    private RequisitesDTO requisites;
}
