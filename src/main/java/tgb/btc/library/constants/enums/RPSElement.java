package tgb.btc.library.constants.enums;

public enum RPSElement {

    ROCK,
    PAPER,
    SCISSORS;

    public static RPSElement getRandomElement() {
        RPSElement[] elements = RPSElement.values();
        int x = (int) (Math.random() * elements.length);
        return elements[x];
    }

}
