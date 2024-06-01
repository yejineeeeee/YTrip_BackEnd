package CodeIt.Ytrip.review.service;

import CodeIt.Ytrip.common.dto.BasePageDto;
import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.review.domain.Review;
import CodeIt.Ytrip.review.dto.ReviewDto;
import CodeIt.Ytrip.review.dto.ReviewPageResponse;
import CodeIt.Ytrip.review.dto.SaveReviewDto;
import CodeIt.Ytrip.review.repository.ReviewRepository;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.user.repository.UserRepository;
import CodeIt.Ytrip.video.domain.Video;
import CodeIt.Ytrip.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

   @Transactional(readOnly = true)
    public ResponseEntity<?> getReviewList(Long videoId, String sort, int page) {
        Pageable pageable= PageRequest.of(page, 10);
        Page<Review> reviews = null;

        if (sort.equals("score")) {
            reviews = reviewRepository.findByVideoIdOrderByScoreDesc(videoId, pageable);
        }

        if (sort.equals("latest")) {
            reviews = reviewRepository.findByVideoIdOrderByCreatedAtDesc(videoId, pageable);
        }

        if (reviews == null || reviews.isEmpty()) {
            System.out.println("reviews is null or empty");
            throw new NoSuchElementException(StatusCode.REVIEW_NOT_FOUND);
        }

        Page<ReviewDto> reviewDtoPage = reviews.map(ReviewDto::from);
        BasePageDto basePageDto = BasePageDto.from(reviewDtoPage);

        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), ReviewPageResponse.of(reviewDtoPage.getContent(), basePageDto)));
    }

    public ResponseEntity<?> saveReview(Long videoId, String email, SaveReviewDto saveReviewDto) {
        User user = findUserByEmail(email);
        Video video = findVideoById(videoId);

        Review review = Review.of(user, video, saveReviewDto);
        log.info("Review = {}", review);
        reviewRepository.save(review);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
    }

    private User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserException(StatusCode.USER_NOT_FOUND));
    }

    private Video findVideoById(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new NoSuchElementException(StatusCode.VIDEO_NOT_FOUND));
    }
}
