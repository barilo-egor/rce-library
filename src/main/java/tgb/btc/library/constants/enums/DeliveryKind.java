package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.IPropertiesPath;
import tgb.btc.library.interfaces.Module;

public enum DeliveryKind implements Module {
    NONE,
    STANDARD;

    @Override
    public boolean isCurrent() {
        return this.equals(DeliveryKind.valueOf(IPropertiesPath.MODULES_PROPERTIES.getString("delivery.kind", "NONE")));
    }

}
