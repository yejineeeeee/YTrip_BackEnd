package CodeIt.Ytrip.video.domain;

import CodeIt.Ytrip.like.domain.VideoLike;
import CodeIt.Ytrip.review.domain.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Video {

    @Id @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    private String title;
    private String content;
    private String url;

    @Column(name = "likes_count")
    private int likeCount;

    private String tag;

    @OneToMany(mappedBy = "video")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "video")
    private List<VideoLike>  videoLikes = new ArrayList<>();

    public static Video of(String title, String content, String url, Integer likeCount, String tag) {
       return Video.builder()
               .title(title)
               .content(content)
               .url(url)
               .likeCount(likeCount)
               .tag(tag)
               .build();
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }
}
