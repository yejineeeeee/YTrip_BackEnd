package CodeIt.Ytrip.video.controller;

import CodeIt.Ytrip.common.JwtUtils;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.video.service.VideoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/video")
public class VideoController {

    private final VideoService videoService;
    private final JwtUtils jwtUtils;

    @GetMapping
    public ResponseEntity<?> getVideoList(
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String tag

    ) {
        return videoService.getVideoList(sort, page, tag);
    }

    @GetMapping("/{video_id}")
    public ResponseEntity<?> getVideoDetailInfo(@PathVariable("video_id") Long videoId) {
        return videoService.getVideoDetailInfo(videoId);
    }

    @PostMapping("/{video_id}/likes")
    public ResponseEntity<?> VideoLike(@PathVariable("video_id") Long videoId, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = jwtUtils.splitBearerToken(bearerToken);
        String email = (String) jwtUtils.getClaims(token).get("email");

        videoService.VideoLike(videoId, email);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
    }

    @GetMapping("/top-liked")
    public ResponseEntity<?> getVideoLikesFive(@RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(
                SuccessResponse.of(
                        StatusCode.SUCCESS.getCode(),
                        StatusCode.SUCCESS.getMessage(),
                        videoService.getTopLikedVideo(limit)
                )
        );
    }
}
