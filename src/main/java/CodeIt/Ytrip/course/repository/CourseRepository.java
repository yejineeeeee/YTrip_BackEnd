package CodeIt.Ytrip.course.repository;

import CodeIt.Ytrip.course.domain.VideoCourse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<VideoCourse, Long> {

    Optional<VideoCourse> findByVideoId(Long videoId);
}
