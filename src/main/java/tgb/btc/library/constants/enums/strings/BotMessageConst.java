package tgb.btc.library.constants.enums.strings;

public enum BotMessageConst {
    FROM_REFERRAL_BALANCE_PURCHASE("На реферальный баланс было добавлено %s₽ по сделке партнера."),
    DEAL_CONFIRMED("Заявка обработана, деньги отправлены.");

    final String message;

    BotMessageConst(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
