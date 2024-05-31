package CodeIt.Ytrip.common.exception;

import CodeIt.Ytrip.common.reponse.StatusCode;
import lombok.Getter;

@Getter
public class UserException extends BaseException{

    public UserException(StatusCode statusCode) {
        super(statusCode);
    }
}