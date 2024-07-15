package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.IPropertiesPath;
import tgb.btc.library.interfaces.Module;

public enum ReferralType implements Module {
    NONE,
    STANDARD;

    public static final ReferralType CURRENT =
            ReferralType.valueOf(IPropertiesPath.MODULES_PROPERTIES.getString("referral.type"));

    @Override
    public boolean isCurrent() {
        return this.equals(CURRENT);
    }
}
