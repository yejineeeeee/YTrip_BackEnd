package CodeIt.Ytrip.video.repository;

import CodeIt.Ytrip.like.domain.VideoLike;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.video.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {
    Optional<VideoLike> findByUserAndVideo(User user, Video video);
    void deleteByUserAndVideo(User user, Video video);
}
