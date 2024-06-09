package CodeIt.Ytrip.review.domain;

import CodeIt.Ytrip.common.domain.BaseEntity;
import CodeIt.Ytrip.like.domain.ReviewLike;
import CodeIt.Ytrip.review.dto.SaveReviewDto;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.video.domain.Video;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class Review extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "review_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToMany(mappedBy = "review")
    private List<ReviewLike> reviewLikes = new ArrayList<>();

    private String title;
    private String content;
    private int score;

    public static Review of(User user, Video video, String content) {
        return Review.builder()
                .user(user)
                .video(video)
                .content(content)
                .build();
    }

    public static Review of(User user, Video video, SaveReviewDto saveReviewDto) {
        return Review.builder()
                .user(user)
                .video(video)
                .content(saveReviewDto.getContent())
                .score(saveReviewDto.getScore())
                .title(saveReviewDto.getTitle())
                .build();
    }

//    public static Review from(SaveReviewDto saveReviewDto) {
//        return Review.builder()
//                .user()
//    }
}
