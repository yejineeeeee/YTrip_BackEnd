package CodeIt.Ytrip.common.reponse;

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
    TOKEN_IS_TAMPERED("4005", "토큰이 위조되었습니다."),
    VIDEO_NOT_FOUND("4006", "해당 비디오를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND("4007", "리뷰를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR("5000", "일시적인 오류입니다. 잠시후 다시 시도해 주세요.");

    private final String code;
    private final String message;
}