package tgb.btc.library.constants.enums.web.merchant.payfinity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PayFinityOrderType {
    CARD("Карта", "CARD", "ANY_BANK"),
    SBP("СБП", "SBP", "ANY_BANK"),
    CROSS_BORDER_SBP("Трансгран СБП", "SBP", "ANY_BANK"),
    VTB_SBP("ВТБ СБП", "SBP", "VTB"),
    SBER_CARD("Сбер карта", "CARD", "SBER"),
    ALFA("Альфа СБП", "SBP", "ALFA"),
    ;

    final String description;

    final String type;

    final String bank;
}
