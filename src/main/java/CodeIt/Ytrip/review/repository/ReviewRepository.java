package CodeIt.Ytrip.review.repository;

import CodeIt.Ytrip.review.domain.Review;
import CodeIt.Ytrip.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByVideoId(Long videoId);
    Page<Review> findByVideoIdOrderByScoreDesc(Long videoId, Pageable pageable);
    Page<Review> findByVideoIdOrderByCreatedAtDesc(Long videoId, Pageable pageable);

}
