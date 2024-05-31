package CodeIt.Ytrip.common.exception;

import CodeIt.Ytrip.common.reponse.StatusCode;
import lombok.Getter;

@Getter
public class RuntimeException extends BaseException {
    public RuntimeException(StatusCode statusCode) {
        super(statusCode);
    }
}
