package CodeIt.Ytrip.course.service;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.exception.RuntimeException;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.course.domain.CourseDetail;
import CodeIt.Ytrip.course.domain.UserCourse;
import CodeIt.Ytrip.course.domain.VideoCourse;
import CodeIt.Ytrip.place.dto.PlaceDto;
import CodeIt.Ytrip.course.dto.CourseResponse;
import CodeIt.Ytrip.course.dto.PlanDto;
import CodeIt.Ytrip.course.dto.CourseListDto;
import CodeIt.Ytrip.course.repository.CourseDetailRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class CourseService {

    private final UserCourseRepository userCourseRepository;
    private final VideoCourseRepository videoCourseRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final CourseDetailRepository courseDetailRepository;

    public ResponseEntity<?> postUserCourse(CourseListDto courseListDto, String email) {
        Optional<User> findUser = userRepository.findByEmail(email);
        User user = findUser.orElseThrow(() -> new UserException(StatusCode.USER_NOT_FOUND));

        String courseName = courseListDto.getName();
        UserCourse userCourse = saveUserCourse(user, courseName);

        List<PlanDto> courses = courseListDto.getPlan();
        courses.forEach(course -> {
            int day = course.getDay();
            List<PlaceDto> places = course.getPlace();
            String placesString = generatePlacesString(places);
            CourseDetail courseDetail = CourseDetail.builder()
                    .places(placesString)
                    .userCourse(userCourse)
                    .dayNum(day)
                    .build();
            courseDetailRepository.save(courseDetail);
        });

        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
    }

    private String generatePlacesString(List<PlaceDto> courses) {
        return courses.stream().map(this::getPlaceId).collect(Collectors.joining(","));
    }

    private String getPlaceId(PlaceDto course) {
        double posX = course.getPosX();
        double posY = course.getPosY();
        Optional<Place> findPlace = placeRepository.findByPxAndPy(posX, posY);
        if (findPlace.isEmpty()) {
            Place place = Place.builder()
                    .name(course.getName())
                    .px(posX)
                    .py(posY)
                    .img(course.getImg())
                    .description(course.getDescription())
                    .build();
            placeRepository.save(place);
            return String.valueOf(place.getId());
        }
        return String.valueOf(findPlace.get().getId());
    }

    private UserCourse saveUserCourse(User user, String courseName) {
        try {
            UserCourse userCourse = UserCourse.builder()
                    .user(user)
                    .name(courseName)
                    .build();
            return userCourseRepository.save(userCourse);
        } catch (Exception e) {
            throw new RuntimeException(StatusCode.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseEntity<?> getVideoCourse(Long videoId) {
        Optional<VideoCourse> findVideoCourse = videoCourseRepository.findByVideoId(videoId);
        findVideoCourse.orElseThrow(() -> new NoSuchElementException(StatusCode.VIDEO_NOT_FOUND));

        List<String> findPlaceIds = List.of(findVideoCourse.get().getPlaces().split(","));

        AtomicInteger index = new AtomicInteger();
        List<PlaceDto> placeDto = findPlaceIds.stream().map(placeId -> {
            index.getAndIncrement();
            Optional<Place> findPlace = placeRepository.findById(Long.parseLong(placeId));
            findPlace.orElseThrow(() -> new NoSuchElementException(StatusCode.PLACE_NOT_FOUND));
            return PlaceDto.of(index.get(), findPlace.get());
        }).toList();

        CourseResponse response = CourseResponse.from(placeDto);
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), response));
    }
}
