package tgb.btc.library.constants.enums.bot;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.constants.enums.properties.PropertiesPath;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public enum DeliveryType implements ObjectNodeConvertable<DeliveryType> {
    VIP(PropertiesPath.DESIGN_PROPERTIES.getString("VIP")),
    STANDARD(PropertiesPath.DESIGN_PROPERTIES.getString("STANDARD"));

    final String displayName;

    DeliveryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Function<DeliveryType, ObjectNode> mapFunction() {
        return deliveryType ->  JacksonUtil.getEmpty()
                .put("name", deliveryType.name())
                .put("displayName", deliveryType.getDisplayName());
    }
}
