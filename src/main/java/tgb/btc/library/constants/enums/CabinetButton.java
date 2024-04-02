package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.PropertiesPath;

public enum CabinetButton {

    ACTIVE_REQUESTS(PropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("ACTIVE_REQUESTS")),
    STATISTICS(PropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("STATISTICS")),
    PROMO_CODE(PropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("PROMO_CODE")),
    BALANCE(PropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("BALANCE")),
    DEALS_HISTORY(PropertiesPath.BUTTONS_DESIGN_PROPERTIES.getString("DEALS_HISTORY"));

    private final String text;

    CabinetButton(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
