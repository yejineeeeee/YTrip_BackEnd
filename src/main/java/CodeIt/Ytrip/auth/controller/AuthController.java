package CodeIt.Ytrip.auth.controller;

import CodeIt.Ytrip.auth.dto.request.LocalLoginRequest;
import CodeIt.Ytrip.auth.dto.request.RegisterRequest;
import CodeIt.Ytrip.auth.service.AuthService;
import CodeIt.Ytrip.auth.service.SocialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SocialService socialService;
    @PostMapping("/kakao/login")
    public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        return socialService.kakaoLogin(code);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/duplicate/email")
    public ResponseEntity<?> checkEmailDuplicate(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("email = {}", email);
        return authService.checkEmailDuplicate(email);
    }

    @PostMapping("/login")
    public ResponseEntity<?> localLogin(@RequestBody LocalLoginRequest localLoginRequest) {
        return authService.localLogin(localLoginRequest);
    }

    @PatchMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestBody Map<String, String> body) {
        String refreshToken = body.get("refresh_token");
        return authService.reissue(refreshToken);
    }
    @PostMapping ("/naver/login")
    public ResponseEntity<?> naverLogin(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        return socialService.naverLogin(code);
    }
}
