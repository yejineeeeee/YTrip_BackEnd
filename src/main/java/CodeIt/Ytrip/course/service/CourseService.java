package CodeIt.Ytrip.course.service;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.exception.RuntimeException;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.course.domain.UserCourse;
import CodeIt.Ytrip.course.domain.VideoCourse;
import CodeIt.Ytrip.course.dto.CourseDto;
import CodeIt.Ytrip.course.dto.CourseResponse;
import CodeIt.Ytrip.course.dto.PostCourseRequest;
import CodeIt.Ytrip.course.repository.UserCourseRepository;
import CodeIt.Ytrip.course.repository.VideoCourseRepository;
import CodeIt.Ytrip.place.domain.Place;
import CodeIt.Ytrip.place.repository.PlaceRepository;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {

    private final UserCourseRepository userCourseRepository;
    private final VideoCourseRepository videoCourseRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;

    public ResponseEntity<?> postUserCourse(PostCourseRequest postCourseRequest) {
        Optional<User> findUser = userRepository.findById(postCourseRequest.getUserId());
        User user = findUser.orElseThrow(() -> new UserException(StatusCode.USER_NOT_FOUND));

        List<CourseDto> courses = postCourseRequest.getCourse();
        String places = generatePlacesString(courses);

        saveUserCourse(user, places);

        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
    }

    private String generatePlacesString(List<CourseDto> courses) {
        return courses.stream().map(this::getPlaceId).collect(Collectors.joining(","));
    }

    private String getPlaceId(CourseDto course) {
        float posX = course.getPosX();
        float posY = course.getPosY();
        Optional<Place> findPlace = placeRepository.findByPosXAndPosY(posX, posY);
        if (findPlace.isEmpty()) {
            Place place = Place.builder()
                    .name(course.getName())
                    .posX(posX)
                    .posY(posY)
                    .build();
            placeRepository.save(place);
            return String.valueOf(place.getId());
        }
        return String.valueOf(findPlace.get().getId());
    }

    private void saveUserCourse(User user, String places) {
        try {
            UserCourse userCourse = UserCourse.builder()
                    .user(user)
                    .places(places)
                    .build();
            userCourseRepository.save(userCourse);
        } catch (Exception e) {
            throw new RuntimeException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getVideoCourse(Long videoId) {
        Optional<VideoCourse> findVideoCourse = videoCourseRepository.findByVideoId(videoId);
        findVideoCourse.orElseThrow(() -> new NoSuchElementException(StatusCode.VIDEO_NOT_FOUND));

        List<String> findPlaceIds = List.of(findVideoCourse.get().getPlaces().split(","));

        AtomicInteger index = new AtomicInteger();
        List<CourseDto> courseDto = findPlaceIds.stream().map(placeId -> {
            index.getAndIncrement();
            Optional<Place> findPlace = placeRepository.findById(Long.parseLong(placeId));
            findPlace.orElseThrow(() -> new NoSuchElementException(StatusCode.PLACE_NOT_FOUND));
            return CourseDto.of(index.get(), findPlace.get());
        }).toList();

        CourseResponse response = CourseResponse.from(courseDto);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), response));
    }
}
