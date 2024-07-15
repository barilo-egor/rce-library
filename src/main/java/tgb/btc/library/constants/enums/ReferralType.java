package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.interfaces.Module;

public enum ReferralType implements Module {
    NONE,
    STANDARD;

    public static final ReferralType CURRENT =
            ReferralType.valueOf(PropertiesPath.MODULES_PROPERTIES.getString("referral.type"));

    @Override
    public boolean isCurrent() {
        return this.equals(CURRENT);
    }
}
