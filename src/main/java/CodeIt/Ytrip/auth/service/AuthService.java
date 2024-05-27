package CodeIt.Ytrip.auth.service;

import CodeIt.Ytrip.auth.dto.TokenDto;
import CodeIt.Ytrip.auth.dto.KakaoUserInfoDto;
import CodeIt.Ytrip.auth.dto.request.LocalLoginRequest;
import CodeIt.Ytrip.auth.dto.request.RegisterRequest;
import CodeIt.Ytrip.common.exception.RuntimeException;
import CodeIt.Ytrip.common.exception.StatusCode;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.JwtUtils;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${KAKAO.REDIRECT.URI}")
    private String redirectUri;
    @Value("${KAKAO.REST.API.KEY}")
    private String restAPiKey;
    private final JwtUtils jwtUtils;

    private final UserRepository userRepository;

    public ResponseEntity<?> kakaoLogin(String code) {
        KakaoUserInfoDto kakaoUserInfo = getAccessToken(code);
        User user = new User();
        Optional<User> findUser = userRepository.findByEmail(kakaoUserInfo.getEmail());
        if (findUser.isPresent()) {
           user.updateRefreshToken(kakaoUserInfo.getRefreshToken());
        } else {
            user.createUser(
                    kakaoUserInfo.getNickName(),
                    kakaoUserInfo.getEmail(),
                    null,
                    kakaoUserInfo.getRefreshToken()
            );
        }
        userRepository.save(user);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), kakaoUserInfo));
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

            TokenDto tokenDto = new TokenDto(
                    (String) jsonObject.get("access_token"),
                    (String) jsonObject.get("refresh_token"));

            return getKakaoUserInfo(tokenDto);

        } catch (ParseException e) {
            throw new RuntimeException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }


    private KakaoUserInfoDto getKakaoUserInfo(TokenDto tokenDto) throws ParseException {
        log.info("KAKAO TOKEN DTO = {}", tokenDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenDto.getAccessToken());
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
        String accessToken = jwtUtils.generateToken(email, 1000 * 60 * 60, "AccessToken");
        String refreshToken = jwtUtils.generateToken(email, 1000 * 60 * 60 * 24, "RefreshToken");
        log.info("Nickname = {}, email = {}, accessToken = {}, refreshToken = {}", nickname, email, accessToken, refreshToken);

        return new KakaoUserInfoDto(nickname, email, accessToken, refreshToken);
    }

    public ResponseEntity<?> register(RegisterRequest registerRequest) {

        String nickname = registerRequest.getNickname();
        String email = registerRequest.getEmail();
        String password = registerRequest.getPassword();
        User user = new User();
        user.createUser(nickname,email,password,null);
        userRepository.save(user);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
    }

    public ResponseEntity<?> checkEmailDuplicate(String email) {
        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
        }
        throw new UserException(StatusCode.DUPLICATE_EMAIL);
    }

    public ResponseEntity<?> localLogin(LocalLoginRequest localLoginRequest) {
        Optional<User> findUser = userRepository.findByEmailAndPassword(localLoginRequest.getEmail(), localLoginRequest.getPassword());
        if (findUser.isPresent()) {
            String accessToken = jwtUtils.generateToken(findUser.get().getEmail(), 1000 * 60 * 60, "AccessToken");
            String refreshToken = jwtUtils.generateToken(findUser.get().getEmail(), 1000 * 60 * 60 * 24, "RefreshToken");
            findUser.get().updateRefreshToken(refreshToken);
            userRepository.save(findUser.get());
            TokenDto tokenDto = new TokenDto(accessToken, refreshToken);
            return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), tokenDto));
        } else {
            throw new UserException(StatusCode.USER_NOT_FOUND);
        }
    }

    public ResponseEntity<?> reissue(String token) {
        if (jwtUtils.isValidToken(token)) {
            Optional<User> findUser = userRepository.findByRefreshToken(token);
            if (findUser.isPresent()) {
                String accessToken = jwtUtils.generateToken(findUser.get().getEmail(), 1000 * 60 * 60, "AccessToken");
                String refreshToken = jwtUtils.generateToken(findUser.get().getEmail(), 1000 * 60 * 60 * 24, "RefreshToken");
                findUser.get().updateRefreshToken(refreshToken);
                TokenDto tokenDto = new TokenDto(accessToken, refreshToken);
                return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), tokenDto));
            } else {
                throw new UserException(StatusCode.USER_NOT_FOUND);
            }
        }
        throw new UserException(StatusCode.LOGIN_REQUIRED);
    }
}