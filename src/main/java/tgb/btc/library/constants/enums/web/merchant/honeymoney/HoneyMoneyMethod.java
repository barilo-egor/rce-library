package tgb.btc.library.constants.enums.web.merchant.honeymoney;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum HoneyMoneyMethod {
    CARD("Карта", null),
    SBP("СБП", null),
    CROSS_BORDER("Трансгран", null),
    SBER_ACCOUNT("Сбер номер счета", "Сбер");

    private final String description;

    private final String bank;
}
