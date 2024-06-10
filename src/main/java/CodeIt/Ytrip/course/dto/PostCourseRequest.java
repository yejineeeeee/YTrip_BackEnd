package CodeIt.Ytrip.course.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PostCourseRequest {

    private Long userId;
    private List<CourseDto> course;

//    public static PostCourseRequest of (Long userId, List<CourseDto> courseDto) {
//        return PostCourseRequest.builder()
//                .userId(userId)
//                .course(courseDto)
//                .build();
//    }
}
