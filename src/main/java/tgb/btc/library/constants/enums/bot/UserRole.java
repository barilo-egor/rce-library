package tgb.btc.library.constants.enums.bot;

import lombok.Getter;

import java.util.Set;

@Getter
public enum UserRole {
    USER("Пользователь"),
    OBSERVER("Наблюдатель"),
    CHAT_ADMIN("Администратор чата"),
    OPERATOR("Оператор"),
    ADMIN("Администратор");

    public static final Set<UserRole> USER_ACCESS = Set.of(UserRole.USER, UserRole.OBSERVER, UserRole.OPERATOR, UserRole.ADMIN);

    public static final Set<UserRole> OBSERVER_ACCESS = Set.of(UserRole.OBSERVER, UserRole.OPERATOR, UserRole.ADMIN);

    public static final Set<UserRole> OPERATOR_ACCESS = Set.of(UserRole.OPERATOR, UserRole.ADMIN);

    public static final Set<UserRole> CHAT_ADMIN_ACCESS = Set.of(UserRole.CHAT_ADMIN, UserRole.ADMIN, UserRole.OPERATOR);

    public static final Set<UserRole> ADMIN_ACCESS = Set.of(UserRole.ADMIN);

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
}
