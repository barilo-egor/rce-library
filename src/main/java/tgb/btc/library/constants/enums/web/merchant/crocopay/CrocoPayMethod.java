package tgb.btc.library.constants.enums.web.merchant.crocopay;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CrocoPayMethod {
    TO_CARD("Карта"),
    SBP("СБП");

    private final String description;
}
