package CodeIt.Ytrip.common.exception;

import lombok.Getter;

@Getter
public class RuntimeException extends java.lang.RuntimeException {
    private String status;
    public RuntimeException() {
        super();
    }

    public RuntimeException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.status = statusCode.getCode();
    }

    public RuntimeException(String message) {
        super(message);
    }

    public RuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RuntimeException(Throwable cause) {
        super(cause);
    }

    protected RuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
