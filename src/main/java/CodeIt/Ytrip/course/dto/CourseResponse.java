package CodeIt.Ytrip.course.dto;

import CodeIt.Ytrip.place.domain.Place;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CourseResponse {
    private List<CourseDto> place;

    public static CourseResponse from(List<CourseDto> courseDto) {
        return CourseResponse.builder()
                .place(courseDto)
                .build();
    }
}
