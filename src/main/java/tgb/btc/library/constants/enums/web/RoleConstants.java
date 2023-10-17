package tgb.btc.library.constants.enums.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.function.Function;

public enum RoleConstants implements ObjectNodeConvertable<RoleConstants> {
    ROLE_USER("Пользователь"),
    ROLE_OPERATOR("Оператор"),
    ROLE_ADMIN("Администратор");

    final String displayName;

    RoleConstants(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public Function<RoleConstants, ObjectNode> mapFunction() {
        return roleConstants -> JacksonUtil.getEmpty()
                .put("name", roleConstants.name())
                .put("displayName", roleConstants.getDisplayName());
    }
}
