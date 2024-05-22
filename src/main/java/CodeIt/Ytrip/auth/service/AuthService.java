package CodeIt.Ytrip.auth.service;

import CodeIt.Ytrip.auth.dto.KakaoTokenDto;
import CodeIt.Ytrip.auth.dto.KakaoUserInfoDto;
import CodeIt.Ytrip.auth.dto.request.LocalLoginRequest;
import CodeIt.Ytrip.auth.dto.request.RegisterRequest;
import CodeIt.Ytrip.auth.dto.response.KakaoLoginResponse;
import CodeIt.Ytrip.auth.dto.response.LocalLoginSuccessResponse;
import CodeIt.Ytrip.auth.dto.response.RegisterResponse;
import CodeIt.Ytrip.common.ErrorResponse;
import CodeIt.Ytrip.common.ResponseStatus;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${KAKAO.REDIRECT.URI}")
    private String redirectUri;
    @Value("${KAKAO.REST.API.KEY}")
    private String restAPiKey;

    private final UserRepository userRepository;

    public KakaoLoginResponse kakaoLogin(String code) {
        KakaoUserInfoDto kakaoUserInfo = getAccessToken(code);

        User user = new User();
        user.createUser(
                kakaoUserInfo.getNickName(),
                kakaoUserInfo.getNickName(),
                kakaoUserInfo.getEmail(),
                null,
                kakaoUserInfo.getRefreshToken()
        );

        userRepository.save(user);
        return new KakaoLoginResponse(1000, kakaoUserInfo.getAccessToken());
    }

    private KakaoUserInfoDto getAccessToken(String code) {
        log.info("code = {}", code);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "authorization_code");
            body.add("client_id", restAPiKey);
            body.add("redirect_uri", redirectUri);
            body.add("code", code);

            HttpEntity<MultiValueMap<String, String>> kakaoRequest = new HttpEntity<>(body, headers);
            log.info("KAKAO REQUEST = {}", kakaoRequest);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> kakaoToken = restTemplate.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoRequest,
                    String.class
            );
            System.out.println("kakaoToken = " + kakaoToken.getBody());
            log.info("KAKAO TOKEN ={}", kakaoToken);

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(kakaoToken.getBody());

            KakaoTokenDto kakaoTokenDto = new KakaoTokenDto(
                    (String) jsonObject.get("access_token"),
                    (String) jsonObject.get("refresh_token"));

            return getKakaoUserInfo(kakaoTokenDto);

        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return null;
    }


    private KakaoUserInfoDto getKakaoUserInfo(KakaoTokenDto kakaoTokenDto) throws ParseException {
        log.info("KAKAO TOKEN DTO = {}",kakaoTokenDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(kakaoTokenDto.getAccessToken());
        headers.setContentType(MediaType.valueOf("application/x-www-form-urlencoded;charset=utf-8"));
        HttpEntity body = new HttpEntity(null, headers);

        log.info("Get Kakao User Info API Http Body = {}", body);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> kakaoUser = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                body,
                String.class
        );
        log.info("KAKAO USER = {}", kakaoUser);

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(kakaoUser.getBody());
        JSONObject kakaoAccount = (JSONObject) jsonObject.get("kakao_account");
        JSONObject profile = (JSONObject) kakaoAccount.get("profile");
        String nickname = String.valueOf(profile.get("nickname"));
        String email = String.valueOf(kakaoAccount.get("email"));
        String accessToken = kakaoTokenDto.getAccessToken();
        String refreshToken = kakaoTokenDto.getRefreshToken();

        log.info("Nickname = {}, email = {}, accessToken = {}, refreshToken = {}", nickname, email, accessToken, refreshToken);

        return new KakaoUserInfoDto(nickname, email, accessToken, refreshToken);
    }

    public RegisterResponse register(RegisterRequest registerRequest) {

        String username = registerRequest.getUsername();
        String nickname = registerRequest.getNickname();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();

        if (!userRepository.existsByEmail(email)) {

            User user = new User();
            // RefeshToken 의 경우 자체 JWT 생성 후 추가 예정
            user.createUser(
                    username,
                    nickname,
                    email,
                    password,
                    null
            );
            userRepository.save(user);
            return new RegisterResponse(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage());
        }

        return new RegisterResponse(ResponseStatus.DUPLICATE_EMAIL.getStatus(), ResponseStatus.DUPLICATE_EMAIL.getMessage());
    }

    public Object localLogin(LocalLoginRequest localLoginRequest) {
        if (userRepository.existsByEmailAndPassword(localLoginRequest.getEmail(), localLoginRequest.getPassword())) {
            return new LocalLoginSuccessResponse(ResponseStatus.SUCCESS.getStatus(), ResponseStatus.SUCCESS.getMessage(), "accessToken");
        } else {
            return new ErrorResponse(ResponseStatus.NOT_EXIST_USER.getStatus(), ResponseStatus.NOT_EXIST_USER.getMessage());
        }
    }
}
