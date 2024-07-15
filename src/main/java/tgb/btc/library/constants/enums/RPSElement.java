package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.IPropertiesPath;

public enum RPSElement {

    ROCK(IPropertiesPath.RPS_MESSAGE.getString("rock")),
    PAPER(IPropertiesPath.RPS_MESSAGE.getString("paper")),
    SCISSORS(IPropertiesPath.RPS_MESSAGE.getString("scissors"));

    private final String symbol;

    RPSElement(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static RPSElement getRandomElement() {
        RPSElement[] elements = RPSElement.values();
        int x = (int) (Math.random() * elements.length);
        return elements[x];
    }

}
