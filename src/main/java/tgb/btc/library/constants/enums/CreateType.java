package tgb.btc.library.constants.enums;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public enum CreateType implements ObjectNodeConvertable<CreateType> {
    BOT("Бот"),
    MANUAL("Ручная");

    final String displayName;

    CreateType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Function<CreateType, ObjectNode> mapFunction() {
        return createType -> JacksonUtil.getEmpty()
                .put("name", createType.name())
                .put("displayName", createType.getDisplayName());
    }
}
