package CodeIt.Ytrip.video.service;

import CodeIt.Ytrip.common.exception.BaseException;
import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.like.domain.VideoLike;
import CodeIt.Ytrip.review.domain.Review;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.user.repository.UserRepository;
import CodeIt.Ytrip.video.domain.Video;
import CodeIt.Ytrip.video.dto.VideoInfoDto;
import CodeIt.Ytrip.video.dto.VideoListDto;
import CodeIt.Ytrip.like.repository.VideoLikeRepository;
import CodeIt.Ytrip.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoLikeRepository videoLikeRepository;
    private Review jdbcTemplate;

    @Transactional(readOnly = true)
    public List<VideoListDto> getAllVideos() {
        List<Video> videos = videoRepository.findAll();

        if (videos.isEmpty()) {
            throw new BaseException(StatusCode.VIDEO_NOT_FOUND);
        }

        return videos.stream()
                .map(VideoListDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VideoListDto> getVideosByTag(String tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Video> videos = videoRepository.findByTagsContaining(tag, pageable);

        if (videos.isEmpty()) {
            throw new BaseException(StatusCode.VIDEO_NOT_FOUND);
        }

        return videos.stream()
                .map(VideoListDto::from)
                .toList();
    }

    public void addTagsToVideo(Long videoId, String tags) {
        Video video = videoRepository.findById(videoId)
                .orElseThrow(() -> new BaseException(StatusCode.VIDEO_NOT_FOUND));

        String[] tagArray = tags.split(",");
        List<String> tagList = Arrays.asList(tagArray);

        video.getTags().clear();
        video.getTags().addAll(tagList);

        videoRepository.save(video);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> getVideoDetailInfo(Long videoId) {
        Optional<Video> findVideo = videoRepository.findById(videoId);
        findVideo.orElseThrow(() -> new NoSuchElementException(StatusCode.VIDEO_NOT_FOUND));
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), VideoInfoDto.from(findVideo.get())));
    }

    public ResponseEntity<?> VideoLike(Long videoId, String email) {
        Video video = findVideoById(videoId);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException(StatusCode.USER_NOT_FOUND));

        boolean alreadyLiked = videoLikeRepository.existsByVideoIdAndUserId(video.getId(), user.getId());
        if (alreadyLiked) {
            return ResponseEntity.badRequest().body(SuccessResponse.of(StatusCode.ALREADY_LIKED.getCode(), StatusCode.ALREADY_LIKED.getMessage()));
        }

        VideoLike videoLike = new VideoLike(video, user);
        video.getVideoLikes().add(videoLike);

        video.setLikecount(video.getLikeCount() + 1);
        videoRepository.save(video);

        log.info("Video ID = {}, Video like: {}", videoId, video.getLikeCount());

        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage()));
    }

    private Video findVideoById(Long videoId) {
        return videoRepository.findById(videoId).orElseThrow(() -> new NoSuchElementException(StatusCode.VIDEO_NOT_FOUND));
    }

    public List<VideoListDto> getTopLikedVideo(int limit) {
        List<Video> topLikedVideos = videoRepository.findTopByOrderByLikeCountDesc(PageRequest.of(0, limit));
        return topLikedVideos.stream()
                .map(VideoListDto::from)
                .toList();
    }
}
