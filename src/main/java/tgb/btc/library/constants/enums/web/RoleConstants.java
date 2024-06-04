package tgb.btc.library.constants.enums.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import tgb.btc.library.exception.BaseException;
import tgb.btc.library.interfaces.ObjectNodeConvertable;
import tgb.btc.library.util.web.JacksonUtil;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public enum RoleConstants implements ObjectNodeConvertable<RoleConstants> {
    ROLE_USER("Пользователь"),
    ROLE_OPERATOR("Оператор"),
    ROLE_API_CLIENT("АПИ клиент"),
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

    public static final List<RoleConstants> ALL = List.of(RoleConstants.values());
    public static final List<RoleConstants> AVAILABLE_TO_OPERATOR = List.of(ROLE_USER, ROLE_API_CLIENT, ROLE_OPERATOR);

    public static List<RoleConstants> getAvailable(RoleConstants roleConstants) {
        if (Objects.isNull(roleConstants)) return ALL;
        if (RoleConstants.ROLE_ADMIN.equals(roleConstants)) return ALL;
        if (RoleConstants.ROLE_OPERATOR.equals(roleConstants)) return AVAILABLE_TO_OPERATOR;
        throw new BaseException("Доступ к ролям для " + roleConstants.name() + " не настроен.");
    }
}
