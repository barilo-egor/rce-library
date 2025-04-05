package tgb.btc.library.constants.enums.web.merchant.honeymoney;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HoneyMoneyMethod {
    CARD("Карта"),
    SBP("СБП"),
    SBER_ACCOUNT("Сбер номер счета");

    private final String description;
}
