package CodeIt.Ytrip.review.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SaveReviewDto {
    private String title;
    private String nickname;
    private String content;
    private int score;
}

