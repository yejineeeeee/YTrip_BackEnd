package CodeIt.Ytrip.course.repository;

import CodeIt.Ytrip.course.domain.CourseDetail;
import CodeIt.Ytrip.course.domain.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseDetailRepository extends JpaRepository<CourseDetail, Long> {

    List<CourseDetail> findByUserCourseIdIn(List<Long> id);
    List<CourseDetail> findByUserCourseId(Long userCourseId);

    void deleteAllByUserCourseId(Long userCourseId);
}
