package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.interfaces.Module;

public enum DeliveryKind implements Module {
    NONE,
    STANDARD;

    @Override
    public boolean isCurrent() {
        return this.equals(DeliveryKind.valueOf(PropertiesPath.MODULES_PROPERTIES.getString("delivery.kind")));
    }

}
