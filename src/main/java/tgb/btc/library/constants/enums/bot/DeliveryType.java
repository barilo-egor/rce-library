package tgb.btc.library.constants.enums.bot;

public enum DeliveryType {
    VIP("Вип"),
    STANDARD("Обычный");

    final String displayName;

    DeliveryType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
