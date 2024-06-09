package CodeIt.Ytrip.course.domain;

import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.video.domain.Video;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserCourse {

    @Id
    @GeneratedValue
    @Column(name = "user_course_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String places;
}
