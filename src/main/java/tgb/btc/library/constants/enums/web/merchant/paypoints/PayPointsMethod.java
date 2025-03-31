package tgb.btc.library.constants.enums.web.merchant.paypoints;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayPointsMethod {
    CARD("Карта"),
    SBP("СБП"),
    TRANSGRAN_SBP("Трансгран СБП");

    final String displayName;
}
