package CodeIt.Ytrip.review.repository;

import CodeIt.Ytrip.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByVideoId(Long videoId);
    Page<Review> findByVideoIdOrderByLikeCountDesc(Long videoId, Pageable pageable);
    Page<Review> findByVideoIdOrderByCreatedAtDesc(Long videoId, Pageable pageable);
}
