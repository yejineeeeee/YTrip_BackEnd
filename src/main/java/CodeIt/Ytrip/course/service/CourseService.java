package CodeIt.Ytrip.course.service;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.course.domain.VideoCourse;
import CodeIt.Ytrip.course.dto.CourseDto;
import CodeIt.Ytrip.course.dto.CourseResponse;
import CodeIt.Ytrip.course.repository.CourseRepository;
import CodeIt.Ytrip.place.domain.Place;
import CodeIt.Ytrip.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final PlaceRepository placeRepository;

    public ResponseEntity<?> getVideoCourse(Long videoId) {
        Optional<VideoCourse> findVideoCourse = courseRepository.findByVideoId(videoId);
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
