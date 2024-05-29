package CodeIt.Ytrip.video.controller;

import CodeIt.Ytrip.video.service.VideoService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/video")
public class VideoController {

    private final VideoService videoService;
    @GetMapping
    public ResponseEntity<?> getVideoList() {
        return videoService.getVideoList();
    }

    @GetMapping("/{video_id}")
    public ResponseEntity<?> getVideoDetailInfo(@PathVariable("video_id") Long videoId) {
        return videoService.getVideoDetailInfo(videoId);
    }
}
