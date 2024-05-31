package CodeIt.Ytrip.common.exception;

import CodeIt.Ytrip.common.reponse.StatusCode;
import lombok.Getter;

@Getter
public class NoSuchElementException extends BaseException {
    public NoSuchElementException(StatusCode statusCode) {
        super(statusCode);
    }
}
