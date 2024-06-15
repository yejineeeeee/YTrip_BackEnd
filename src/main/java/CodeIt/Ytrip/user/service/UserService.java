package CodeIt.Ytrip.user.service;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.course.domain.CourseDetail;
import CodeIt.Ytrip.course.domain.UserCourse;
import CodeIt.Ytrip.course.dto.CourseDto;
import CodeIt.Ytrip.course.dto.PlanDto;
import CodeIt.Ytrip.place.dto.PlaceDto;
import CodeIt.Ytrip.course.repository.CourseDetailRepository;
import CodeIt.Ytrip.course.repository.UserCourseRepository;
import CodeIt.Ytrip.place.domain.Place;
import CodeIt.Ytrip.place.repository.PlaceRepository;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.user.dto.UserCourseResponse;
import CodeIt.Ytrip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final UserCourseRepository userCourseRepository;

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
}
