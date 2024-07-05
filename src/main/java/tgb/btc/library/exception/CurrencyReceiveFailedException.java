package tgb.btc.library.exception;

public class CurrencyReceiveFailedException extends BaseException {

    public CurrencyReceiveFailedException() {
    }

    public CurrencyReceiveFailedException(String message) {
        super(message);
    }

    public CurrencyReceiveFailedException(String message, Throwable cause) {
        super(message, cause);
    }

}
