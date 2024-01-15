package tgb.btc.library.constants.enums.bot;

import tgb.btc.library.constants.enums.properties.PropertiesPath;

public enum DeliveryType {
    VIP(PropertiesPath.DESIGN_PROPERTIES.getString("VIP")),
    STANDARD(PropertiesPath.DESIGN_PROPERTIES.getString("STANDARD"));

    final String displayName;

    DeliveryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
