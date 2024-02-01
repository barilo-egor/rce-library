package tgb.btc.library.constants.enums;

import tgb.btc.library.constants.enums.properties.PropertiesPath;

public enum RPSElement {

    ROCK(PropertiesPath.RPS_MESSAGE.getString("rock")),
    PAPER(PropertiesPath.RPS_MESSAGE.getString("paper")),
    SCISSORS(PropertiesPath.RPS_MESSAGE.getString("scissors"));

    private final String symbol;

    RPSElement(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

    public static RPSElement getRandomElement() {
        int x = (int) (Math.random() * 3);
        return RPSElement.values()[x];
    }

}
