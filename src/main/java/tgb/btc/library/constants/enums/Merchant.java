package tgb.btc.library.constants.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import tgb.btc.library.constants.enums.properties.VariableType;

@AllArgsConstructor
@Getter
public enum Merchant {
    NONE(null),
    PAYSCROW(VariableType.PAYSCROW_BOUND),
    DASH_PAY(VariableType.DASH_PAY_BOUND),
    ALFA_TEAM(VariableType.ALFA_TEAM_BOUND);

    private final VariableType maxAmount;
}
