package CodeIt.Ytrip.user.dto;

import CodeIt.Ytrip.course.dto.CourseDto;
import CodeIt.Ytrip.course.dto.CourseListDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserCourseResponse {

    private List<CourseDto> course;

    public static UserCourseResponse from(List<CourseDto> course) {
        return UserCourseResponse.builder()
                .course(course)
                .build();
    }

}
