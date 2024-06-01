package CodeIt.Ytrip.review.dto;

import CodeIt.Ytrip.review.domain.Review;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Data
@Builder
public class ReviewDto {
    private Long id;
    private String title;
    private String content;
    private int score;
    private LocalDateTime createdAt;

    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .score(review.getScore())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
