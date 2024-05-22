package CodeIt.Ytrip.auth.dto.response;

import CodeIt.Ytrip.common.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private Integer code;
    private String message;
}
