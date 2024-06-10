package CodeIt.Ytrip.user.service;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.course.domain.UserCourse;
import CodeIt.Ytrip.course.dto.CourseDto;
import CodeIt.Ytrip.course.repository.UserCourseRepository;
import CodeIt.Ytrip.place.domain.Place;
import CodeIt.Ytrip.place.repository.PlaceRepository;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final UserCourseRepository userCourseRepository;

    public ResponseEntity<?> findUserCourse(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        findUser.orElseThrow(() -> new UserException(StatusCode.USER_NOT_FOUND));

        List<UserCourse> findUserCourse = userCourseRepository.findByUserId(userId);

        List<List<CourseDto>> response = findUserCourse.stream().map(coursePlaces -> {
            List<Long> placeIds = Arrays.stream(coursePlaces.getPlaces().split(","))
                    .map(Long::parseLong)
                    .toList();

            AtomicInteger index = new AtomicInteger();
            List<Place> findPlaces = placeRepository.findByIdIn(placeIds);
            return findPlaces.stream().map(course -> CourseDto.of(index.incrementAndGet(), course))
                    .toList();
        }).toList();

        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), response));
    }
}
