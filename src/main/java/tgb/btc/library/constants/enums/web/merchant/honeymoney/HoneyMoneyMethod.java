package tgb.btc.library.constants.enums.web.merchant.honeymoney;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HoneyMoneyMethod {
    CARD("Карта", "bank"),
    SBP("СБП", "bank"),
    SBER_ACCOUNT("Сбер номер счета", "bank");

    private final String description;

    private final String bank;
}
