package tgb.btc.library.exception;

import tgb.btc.library.constants.enums.bot.CryptoCurrency;

public class ReadRubCourseException extends ReadCourseException {

    public ReadRubCourseException() {
    }

    public ReadRubCourseException(String message) {
        super(message);
    }

    public ReadRubCourseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadRubCourseException(CryptoCurrency cryptoCurrency) {
        super(cryptoCurrency);
    }

}
