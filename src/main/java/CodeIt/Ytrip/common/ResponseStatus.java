package CodeIt.Ytrip.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ResponseStatus {
    /**
     * 요청 성공 1000
     */
    SUCCESS(1000, "요청에 성공하였습니다."),

    /**
     * 이메일 중복 2000
     */
    DUPLICATE_EMAIL(2000, "중복된 이메일 입니다.");

    private final Integer status;
    private final String message;

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
