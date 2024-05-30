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
    private Integer likeCount;
    private LocalDateTime createdAt;

    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .likeCount(review.getLikeCount())
                .createdAt(review.getCreatedAt())
                .build();
    }
}
