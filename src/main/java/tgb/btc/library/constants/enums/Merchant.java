package tgb.btc.library.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tgb.btc.library.constants.enums.properties.VariableType;
import tgb.btc.library.constants.enums.web.merchant.alfateam.InvoiceStatus;
import tgb.btc.library.constants.enums.web.merchant.dashpay.DashPayOrderStatus;
import tgb.btc.library.constants.enums.web.merchant.evopay.EvoPayStatus;
import tgb.btc.library.constants.enums.web.merchant.nicepay.NicePayStatus;
import tgb.btc.library.constants.enums.web.merchant.onlypays.OnlyPaysStatus;
import tgb.btc.library.constants.enums.web.merchant.paypoints.PayPointsStatus;
import tgb.btc.library.constants.enums.web.merchant.payscrow.OrderStatus;

@AllArgsConstructor
@Getter
public enum Merchant {
    NONE(null, "none", false),
    PAYSCROW(VariableType.PAYSCROW_BOUND, "Payscrow", true),
    DASH_PAY(VariableType.DASH_PAY_BOUND, "DashPay", true),
    ALFA_TEAM(VariableType.ALFA_TEAM_BOUND, "AlfaTeam", true),
    ALFA_TEAM_TJS(VariableType.ALFA_TEAM_TJS_BOUND, "AlfaTeam TJS", true),
    ALFA_TEAM_VTB(VariableType.ALFA_TEAM_VTB_BOUND, "AlfaTeam VTB", true),
    ALFA_TEAM_ALFA(VariableType.ALFA_TEAM_ALFA_BOUND, "AlfaTeam ALFA", true),
    ALFA_TEAM_SBER(VariableType.ALFA_TEAM_SBER_BOUND, "AlfaTeam SBER", true),
    PAY_POINTS(VariableType.PAY_POINTS_BOUND, "Paypoints", false),
    ONLY_PAYS(VariableType.ONLY_PAYS_BOUND, "OnlyPays", false),
    EVO_PAY(VariableType.EVO_PAY_BOUND, "EvoPay", false),
    NICE_PAY(VariableType.NICE_PAY_BOUND, "NicePay", false),
    HONEY_MONEY(VariableType.HONEY_MONEY_BOUND, "HoneyMoney", false)
    ;

    private final VariableType maxAmount;

    private final String displayName;

    private final boolean isDecimalAmount;

    public String getDisplayStatus(String statusName) {
        return switch (this) {
            case PAYSCROW -> OrderStatus.valueOf(statusName).getDescription();
            case DASH_PAY -> DashPayOrderStatus.valueOf(statusName).getDescription();
            case ALFA_TEAM, ALFA_TEAM_TJS, ALFA_TEAM_VTB, ALFA_TEAM_ALFA, ALFA_TEAM_SBER -> InvoiceStatus.valueOf(statusName).getDescription();
            case PAY_POINTS -> PayPointsStatus.valueOf(statusName).getDisplayName();
            case ONLY_PAYS -> OnlyPaysStatus.valueOf(statusName).getDescription();
            case EVO_PAY -> EvoPayStatus.valueOf(statusName).getDescription();
            case NICE_PAY -> NicePayStatus.valueOf(statusName).getDescription();
            case HONEY_MONEY ->
            default -> null;
        };
    }
}
