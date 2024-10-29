package tgb.btc.library.constants.enums.bot;

import java.util.Set;

public enum UserRole {
    USER("Пользователь"),
    OBSERVER("Наблюдатель"),
    OPERATOR("Оператор"),
    ADMIN("Администратор");

    public static final Set<UserRole> USER_ACCESS = Set.of(UserRole.USER, UserRole.OBSERVER, UserRole.OPERATOR, UserRole.ADMIN);

    public static final Set<UserRole> OBSERVER_ACCESS = Set.of(UserRole.OBSERVER, UserRole.OPERATOR, UserRole.ADMIN);

    public static final Set<UserRole> OPERATOR_ACCESS = Set.of(UserRole.OPERATOR, UserRole.ADMIN);

    public static final Set<UserRole> ADMIN_ACCESS = Set.of(UserRole.ADMIN);

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
