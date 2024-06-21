package CodeIt.Ytrip.admin;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.course.domain.VideoCourse;
import CodeIt.Ytrip.course.repository.VideoCourseRepository;
import CodeIt.Ytrip.like.repository.VideoLikeRepository;
import CodeIt.Ytrip.place.domain.Place;
import CodeIt.Ytrip.place.repository.PlaceRepository;
import CodeIt.Ytrip.review.repository.ReviewRepository;
import CodeIt.Ytrip.video.domain.Video;
import CodeIt.Ytrip.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class AdminService {

    private final ReviewRepository reviewRepository;
    private final VideoRepository videoRepository;
    private final PlaceRepository placeRepository;
    private final VideoLikeRepository videoLikeRepository;
    private final VideoCourseRepository videoCourseRepository;

    public void saveVideoAndVideoCourse(SaveVideoRequest saveVideoRequest) {

        Video video = Video.builder()
                .content(saveVideoRequest.getContent())
                .title(saveVideoRequest.getTitle())
                .tags(saveVideoRequest.getTag())
                .videoUrl(saveVideoRequest.getVideoUrl())
                .imageUrl(saveVideoRequest.getImageUrl())
                .build();
        videoRepository.save(video);

        String placeIds = saveVideoRequest.getPlace().stream().map(p -> {
            Optional<Place> findPlace = placeRepository.findByPxAndPy(p.getPosX(), p.getPosY());

            if (findPlace.isEmpty()) {
                Place place = Place.builder()
                        .description(p.getDescription())
                        .img(p.getImg())
                        .name(p.getName())
                        .px(p.getPosX())
                        .py(p.getPosY())
                        .build();
                return placeRepository.save(place).getId().toString();
            } else {
                return findPlace.get().getId().toString();
            }
        }).collect(Collectors.joining(","));

        VideoCourse videoCourse = VideoCourse.builder()
                .video(video)
                .places(placeIds).build();

        videoCourseRepository.save(videoCourse);
    }

    public void deleteVideo(Long videoId) {
        reviewRepository.deleteByVideoId(videoId);
        videoCourseRepository.deleteByVideoId(videoId);
        videoLikeRepository.deleteByVideoId(videoId);
    }
}
