package CodeIt.Ytrip.review.controller;

import CodeIt.Ytrip.common.JwtUtils;
import CodeIt.Ytrip.review.dto.SaveReviewDto;
import CodeIt.Ytrip.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final JwtUtils jwtUtils;

    @GetMapping("/video/{video-id}/reviews")
    public ResponseEntity<?> getReviewList(
            @PathVariable("video-id") Long videoId,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page
//            @PageableDefault(size = 2) Pageable pageable
    ) {
        return reviewService.getReviewList(videoId, sort, page);
    }

    @PostMapping("/video/{video-id}/review")
    public ResponseEntity<?> saveReview(@PathVariable("video-id") Long videoId, @RequestBody SaveReviewDto saveReviewDto, HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        String token = jwtUtils.splitBearerToken(bearerToken);
        String email = (String) jwtUtils.getClaims(token).get("email");
        return reviewService.saveReview(videoId, email, saveReviewDto);
    }

}
