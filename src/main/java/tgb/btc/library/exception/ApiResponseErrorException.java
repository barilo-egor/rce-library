package tgb.btc.library.exception;

public class ApiResponseErrorException extends BaseException {

    public ApiResponseErrorException() {
    }

    public ApiResponseErrorException(String message) {
        super(message);
    }

    public ApiResponseErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
