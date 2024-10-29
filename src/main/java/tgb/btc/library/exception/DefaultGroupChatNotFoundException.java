package tgb.btc.library.exception;

public class DefaultGroupChatNotFoundException extends Exception {

    public DefaultGroupChatNotFoundException() {
    }

    public DefaultGroupChatNotFoundException(String message) {
        super(message);
    }

    public DefaultGroupChatNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public DefaultGroupChatNotFoundException(Throwable cause) {
        super(cause);
    }

    public DefaultGroupChatNotFoundException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
