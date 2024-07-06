package tgb.btc.library.exception;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;

public class ReadCourseException extends BaseException {

    private CryptoCurrency cryptoCurrency;

    public ReadCourseException() {
    }

    public ReadCourseException(String message) {
        super(message);
    }

    public ReadCourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadCourseException(CryptoCurrency cryptoCurrency) {
        super();
        this.cryptoCurrency = cryptoCurrency;
    }

    public CryptoCurrency getCryptoCurrency() {
        return cryptoCurrency;
    }
}
