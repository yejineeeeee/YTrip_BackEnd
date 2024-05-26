package CodeIt.Ytrip.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    SUCCESS("2000", "요청에 성공하였습니다."),
    TOKEN_EXPIRED("4000", "토큰이 만료되었습니다."),
    LOGIN_REQUIRED("4001", "로그인이 필요합니다."),
    DUPLICATE_EMAIL("4002", "중복된 이메일입니다."),
    USER_NOT_FOUND("4003", "존재하지 않는 유저 입니다."),
    TOKEN_IS_NULL("4004", "토큰이 없습니다."),
    TOKEN_IS_TAMPERED("4005", "토큰이 위조되었습니다.");

    private final String code;
    private final String message;
}
