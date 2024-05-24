package CodeIt.Ytrip.auth.controller;

import CodeIt.Ytrip.auth.dto.request.LocalLoginRequest;
import CodeIt.Ytrip.auth.dto.request.RegisterRequest;
import CodeIt.Ytrip.auth.dto.response.KakaoLoginResponse;
import CodeIt.Ytrip.auth.dto.response.SuccessResponse;
import CodeIt.Ytrip.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    @PostMapping("/kakao/login")
    public KakaoLoginResponse kakaoLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        return authService.kakaoLogin(code);
    }

    @PostMapping("/register")
    public SuccessResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/duplicate/email")
    public Object checkEmailDuplicate(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("email = {}", email);
        return authService.checkEmailDuplicate(email);
    }

    @PostMapping("/login")
    public Object localLogin(@RequestBody LocalLoginRequest localLoginRequest) {
        return authService.localLogin(localLoginRequest);
    }

    @PatchMapping("/reissue")
    public Object reissue(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        return authService.reissue(refreshToken);
    }
}
