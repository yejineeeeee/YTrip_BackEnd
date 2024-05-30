package CodeIt.Ytrip.review.controller;

import CodeIt.Ytrip.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/video/{videoId}/reviews")
    public ResponseEntity<?> getReviewList(
            @PathVariable("videoId") Long videoId,
            @RequestParam(defaultValue = "latest") String sort,
            @RequestParam(defaultValue = "0") int page
//            @PageableDefault(size = 2) Pageable pageable
    ) {
        return reviewService.getReviewList(videoId, sort, page);
    }

}
