package tgb.btc.library.constants.enums.bot;

public enum GameResultType {
    WIN("Выгрыш"),
    LOSE("Проигрыш");

    final String displayName;

    GameResultType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
