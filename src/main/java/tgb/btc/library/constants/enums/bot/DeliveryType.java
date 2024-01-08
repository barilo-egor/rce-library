package tgb.btc.library.constants.enums.bot;

import tgb.btc.library.constants.enums.CommonDesignProperties;

public enum DeliveryType {
    VIP(CommonDesignProperties.DESIGN.getString("VIP")),
    STANDARD(CommonDesignProperties.DESIGN.getString("STANDARD"));

    final String displayName;

    DeliveryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
