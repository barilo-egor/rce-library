package tgb.btc.library.exception;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;

public class ReadUsdCourseException extends ReadCourseException {

    public ReadUsdCourseException() {
    }

    public ReadUsdCourseException(String message) {
        super(message);
    }

    public ReadUsdCourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadUsdCourseException(CryptoCurrency cryptoCurrency) {
        super(cryptoCurrency);
    }

}
