package CodeIt.Ytrip.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocalLoginSuccessResponse {

    private Integer status;
    private String message;
    private String accessToken;
}
