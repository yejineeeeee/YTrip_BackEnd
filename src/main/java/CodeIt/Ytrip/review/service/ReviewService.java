package CodeIt.Ytrip.review.service;

import CodeIt.Ytrip.common.dto.BasePageDto;
import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.review.domain.Review;
import CodeIt.Ytrip.review.dto.ReviewDto;
import CodeIt.Ytrip.review.dto.ReviewPageResponse;
import CodeIt.Ytrip.review.repository.ReviewRepository;
import CodeIt.Ytrip.video.domain.Video;
import CodeIt.Ytrip.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ResponseEntity<?> getReviewList(Long videoId, String sort, int page) {
        Pageable pageable= PageRequest.of(page, 10);
        Page<Review> reviews = null;

        if (sort.equals("likes")) {
            reviews = reviewRepository.findByVideoIdOrderByLikeCountDesc(videoId, pageable);
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
}
