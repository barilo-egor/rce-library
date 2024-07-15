package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.IPropertiesPath;

public enum CabinetButton {

    ACTIVE_REQUESTS(IPropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("ACTIVE_REQUESTS")),
    STATISTICS(IPropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("STATISTICS")),
    PROMO_CODE(IPropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("PROMO_CODE")),
    BALANCE(IPropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("BALANCE")),
    DEALS_HISTORY(IPropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("DEALS_HISTORY"));

    private final String text;

    CabinetButton(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
