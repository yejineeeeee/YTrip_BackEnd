package CodeIt.Ytrip.course.controller;

import CodeIt.Ytrip.course.dto.PostCourseRequest;
import CodeIt.Ytrip.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<?> postUserCourse(@RequestBody PostCourseRequest postCourseRequest) {
        return courseService.postUserCourse(postCourseRequest);
    }
    @GetMapping("/{video_id}")
    public ResponseEntity<?> getVideoCourse(@PathVariable("video_id") Long videoId) {
        return courseService.getVideoCourse(videoId);
    }

}
