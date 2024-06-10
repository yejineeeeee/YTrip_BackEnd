package CodeIt.Ytrip.video.domain;

import CodeIt.Ytrip.common.domain.BaseEntity;
import CodeIt.Ytrip.course.domain.VideoCourse;
import CodeIt.Ytrip.like.domain.VideoLike;
import CodeIt.Ytrip.review.domain.Review;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Video extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    private String title;
    private String content;
    private String url;

    @Column(name = "likes_count")
    private int likeCount;
    public void setLikecount(int likeCount) {
        this.likeCount = likeCount;
    }

    @ElementCollection
    @CollectionTable(name = "video_tags", joinColumns = @JoinColumn(name = "video_id"))
    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @OneToMany(mappedBy = "video")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VideoLike> videoLikes = new ArrayList<>();

    @OneToMany(mappedBy = "video")
    @Builder.Default
    private List<VideoCourse> course = new ArrayList<>();

    public static Video of(String title, String content, String url, int likeCount, List<String> tags) {
        return Video.builder()
                .title(title)
                .content(content)
                .url(url)
                .likeCount(likeCount)
                .tags(tags)
                .build();
    }
}
