package tgb.btc.library.constants.enums;

import com.vdurmont.emoji.EmojiParser;
import tgb.btc.library.constants.enums.properties.PropertiesPath;

public enum SlotValue {

    BAR("BAR"),
    CHERRY(EmojiParser.parseToUnicode(":cherries:")),
    LEMON(EmojiParser.parseToUnicode(":lemon:")),
    SEVEN(EmojiParser.parseToUnicode(":seven:"));

    private final String emojiValue;

    SlotValue(String emojiValue) {
        this.emojiValue = emojiValue;
    }

    public String getEmojiValue() {
        return emojiValue;
    }

    public String getTripleAmount() {
        return PropertiesPath.SLOT_REEL_PROPERTIES.getString(this.name().toLowerCase().concat(".triple"));
    }

    public String getDoubleAmount() {
        return PropertiesPath.SLOT_REEL_PROPERTIES.getString(this.name().toLowerCase().concat(".double"));
    }

    public SlotValue[] getThree() {
        return new SlotValue[]{this, this, this};
    }

    public SlotValue[] getTwo() {
        return new SlotValue[]{this, this};
    }

}
