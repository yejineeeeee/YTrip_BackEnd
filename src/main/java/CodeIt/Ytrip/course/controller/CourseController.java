package CodeIt.Ytrip.course.controller;

import CodeIt.Ytrip.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;
    @GetMapping("/{video_id}")
    public ResponseEntity<?> getVideoCourse(@PathVariable("video_id") Long videoId) {
        return courseService.getVideoCourse(videoId);
    }
}
