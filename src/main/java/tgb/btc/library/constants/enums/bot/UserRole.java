package tgb.btc.library.constants.enums.bot;

public enum UserRole {
    USER("Пользователь"),
    OPERATOR("Оператор"),
    ADMIN("Администратор");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
