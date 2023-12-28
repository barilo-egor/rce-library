package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.CommonProperties;
import tgb.btc.library.interfaces.Module;

public enum DeliveryType implements Module {
    NONE,
    STANDARD;

    @Override
    public boolean isCurrent() {
        return this.equals(DeliveryType.valueOf(CommonProperties.MODULES.getString("delivery.type")));
    }

}
