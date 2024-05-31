package CodeIt.Ytrip.common.exception;

import CodeIt.Ytrip.common.reponse.StatusCode;
import lombok.Getter;

@Getter
public class TokenException extends BaseException {

    public TokenException(StatusCode statusCode) {
        super(statusCode);
    }
}
