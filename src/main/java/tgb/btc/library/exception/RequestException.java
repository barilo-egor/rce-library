package tgb.btc.library.exception;

public class RequestException extends BaseException {
    public RequestException() {
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
