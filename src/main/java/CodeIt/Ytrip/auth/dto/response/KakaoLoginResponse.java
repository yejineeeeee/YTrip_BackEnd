package CodeIt.Ytrip.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoLoginResponse {
    private Integer code;
    private String accessToken;
}
