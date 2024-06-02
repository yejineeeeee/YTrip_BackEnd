package CodeIt.Ytrip.video.repository;

import CodeIt.Ytrip.like.domain.VideoLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoLikeRepository extends JpaRepository<VideoLike, Long> {
    boolean existsByVideoIdAndUserId(Long videoId, Long userId);
}
