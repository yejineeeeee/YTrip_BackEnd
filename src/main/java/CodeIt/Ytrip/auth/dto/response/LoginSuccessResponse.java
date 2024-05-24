package CodeIt.Ytrip.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginSuccessResponse {

    private String status;
    private String message;
    private String accessToken;
    private String refreshToken;
}
