package tgb.btc.library.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.constants.enums.web.merchant.alfateam.InvoiceStatus;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;

@AllArgsConstructor
@Getter
public enum Merchant {
    NONE(null, "none"),
    PAYSCROW(VariableType.PAYSCROW_BOUND, "Payscrow"),
    DASH_PAY(VariableType.DASH_PAY_BOUND, "DashPay"),
    ALFA_TEAM(VariableType.ALFA_TEAM_BOUND, "AlfaTeam"),
    ALFA_TEAM_TJS(VariableType.ALFA_TEAM_TJS_BOUND, "AlfaTeam TJS"),
    ALFA_TEAM_VTB(VariableType.ALFA_TEAM_VTB_BOUND, "AlfaTeam VTB"),
    ALFA_TEAM_ALFA(VariableType.ALFA_TEAM_ALFA_BOUND, "AlfaTeam ALFA"),
    ALFA_TEAM_SBER(VariableType.ALFA_TEAM_SBER_BOUND, "AlfaTeam SBER"),
    ;

    private final VariableType maxAmount;

    private final String displayName;

    public String getDisplayStatus(String statusName) {
        return switch (this) {
            case PAYSCROW -> OrderStatus.valueOf(statusName).getDescription();
            case DASH_PAY -> DashPayOrderStatus.valueOf(statusName).getDescription();
            case ALFA_TEAM, ALFA_TEAM_TJS -> InvoiceStatus.valueOf(statusName).getDescription();
            default -> null;
        };
    }
}
