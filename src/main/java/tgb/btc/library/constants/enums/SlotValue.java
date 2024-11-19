package tgb.btc.library.constants.enums;

public enum SlotValue {

    BAR("BAR"),
    CHERRY("\uD83C\uDF52"),
    LEMON("🍋"),
    SEVEN("7⃣");

    private final String emojiValue;

    SlotValue(String emojiValue) {
        this.emojiValue = emojiValue;
    }

    public String getEmojiValue() {
        return emojiValue;
    }

    public SlotValue[] getThree() {
        return new SlotValue[]{this, this, this};
    }

    public SlotValue[] getTwo() {
        return new SlotValue[]{this, this};
    }

}
