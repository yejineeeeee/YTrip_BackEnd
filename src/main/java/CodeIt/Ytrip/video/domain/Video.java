package CodeIt.Ytrip.video.domain;

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
public class Video {

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
    private String tag;

    @OneToMany(mappedBy = "video")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideoLike>  videoLikes = new ArrayList<>();

    @OneToMany(mappedBy = "video")
    private List<VideoCourse> cours = new ArrayList<>();

    public static Video of(String title, String content, String url, Integer likeCount, String tag) {
       return Video.builder()
               .title(title)
               .content(content)
               .url(url)
               .likeCount(likeCount)
               .tag(tag)
               .build();
    }

}
