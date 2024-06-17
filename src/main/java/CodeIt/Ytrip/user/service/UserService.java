package CodeIt.Ytrip.user.service;

import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.course.domain.CourseDetail;
import CodeIt.Ytrip.course.domain.UserCourse;
import CodeIt.Ytrip.course.dto.CourseDto;
import CodeIt.Ytrip.course.dto.PlanDto;
import CodeIt.Ytrip.like.domain.VideoLike;
import CodeIt.Ytrip.like.repository.VideoLikeRepository;
import CodeIt.Ytrip.place.dto.PlaceDto;
import CodeIt.Ytrip.course.repository.UserCourseRepository;
import CodeIt.Ytrip.place.domain.Place;
import CodeIt.Ytrip.place.repository.PlaceRepository;
import CodeIt.Ytrip.user.dto.UserCourseResponse;
import CodeIt.Ytrip.user.repository.UserRepository;
import CodeIt.Ytrip.video.domain.Video;
import CodeIt.Ytrip.video.dto.VideoListDto;
import CodeIt.Ytrip.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final VideoRepository videoRepository;
    private final UserCourseRepository userCourseRepository;
    private final VideoLikeRepository videoLikeRepository;

    @Transactional(readOnly = true)
    public ResponseEntity<?> findUserCourse(Long userId) {
        userRepository.findById(userId).orElseThrow(
                () -> new UserException(StatusCode.USER_NOT_FOUND)
        );

        List<UserCourse> userCourses = userCourseRepository.findByUserId(userId);
        List<CourseDto> courseDto = userCourses.stream()
                .map(this::convertToCourseDto)
                .toList();

        UserCourseResponse response = UserCourseResponse.from(courseDto);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), response));
    }

    private CourseDto convertToCourseDto(UserCourse userCourse) {
        List<PlanDto> planDto = userCourse.getCourseDetails().stream()
                .map(this::convertToPlanDto)
                .sorted(Comparator.comparingInt(PlanDto::getDay))
                .toList();

        return CourseDto.of(userCourse.getName(),userCourse.getCreatedAt(), userCourse.getUpdatedAt(), planDto);
    }

    private PlanDto convertToPlanDto(CourseDetail courseDetail) {
        List<Long> placeIds = Arrays.stream(courseDetail.getPlaces().split(","))
                .map(Long::parseLong)
                .toList();

        AtomicInteger index = new AtomicInteger();

        List<Place> places = placeRepository.findByIdIn(placeIds);
        List<PlaceDto> placeDto = places.stream()
                .map(place -> PlaceDto.of(index.incrementAndGet(), place))
                .toList();

        return PlanDto.of(courseDetail.getDayNum(), placeDto);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getUserLikeVideo(Long userId) {
        List<Long> videoLikes = videoLikeRepository.findByUserId(userId).stream().map(VideoLike::getId).toList();
        List<Video> findVideos = videoRepository.findByIdIn(videoLikes);
        List<VideoListDto> videoList = findVideos.stream().map(VideoListDto::from).toList();
        System.out.println("videoList = " + videoList);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), videoList));
    }

    public ResponseEntity<?> deleteUserLikeVideo(Long userId, Long videoId) {
        videoLikeRepository.deleteByUserIdAndVideoId(userId, videoId);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
    }
}
