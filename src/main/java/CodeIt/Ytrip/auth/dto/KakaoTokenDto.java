package CodeIt.Ytrip.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KakaoTokenDto {
    private String accessToken;
    private String refreshToken;
}
