package tgb.btc.library.constants.enums.web.merchant.evopay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EvoPayPaymentMethod {
    BANK_CARD("Карта"),
    SBP("СБП");

    final String description;
}
