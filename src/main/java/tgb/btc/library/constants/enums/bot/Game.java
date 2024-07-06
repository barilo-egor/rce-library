package tgb.btc.library.constants.enums.bot;

public enum Game {
    SLOT_REEL("Барабан"),
    RPC("КНБ");

    final String displayName;

    Game(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
