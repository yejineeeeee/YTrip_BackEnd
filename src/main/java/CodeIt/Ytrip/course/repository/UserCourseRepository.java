package CodeIt.Ytrip.course.repository;

import CodeIt.Ytrip.course.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
}
