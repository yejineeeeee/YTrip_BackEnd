package CodeIt.Ytrip.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    /**
     * 요청 성공 1000
     */
    SUCCESS("1000", "요청에 성공하였습니다."),

    /**
     * 이메일 중복 2000
     */
    LOGIN_REQUIRED("2000", "다시 로그인이 필요합니다."),
    DUPLICATE_EMAIL("2001", "중복된 이메일 입니다."),
    NOT_EXIST_USER("2002", "존재하지 않는 유저입니다.");


    private final String status;
    private final String message;

}
