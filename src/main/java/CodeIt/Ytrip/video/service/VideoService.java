package CodeIt.Ytrip.video.service;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
import CodeIt.Ytrip.common.exception.UserException;
import CodeIt.Ytrip.common.reponse.StatusCode;
import CodeIt.Ytrip.common.reponse.SuccessResponse;
import CodeIt.Ytrip.like.domain.VideoLike;
import CodeIt.Ytrip.user.domain.User;
import CodeIt.Ytrip.user.repository.UserRepository;
import CodeIt.Ytrip.video.domain.Video;
import CodeIt.Ytrip.video.dto.VideoInfoDto;
import CodeIt.Ytrip.video.dto.VideoListDto;
import CodeIt.Ytrip.video.repository.VideoLikeRepository;
import CodeIt.Ytrip.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRepository userRepository;
    private final VideoLikeRepository videoLikeRepository;

    public ResponseEntity<?> getVideoList() {
        List<VideoListDto> videos = videoRepository.findTop12ByOrderByLikeCountDesc()
                .stream().map(VideoListDto::from)
                .toList();

        log.info("Video size = {}", videos.size());

        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), videos));
    }

    public ResponseEntity<?> getVideoDetailInfo(Long videoId) {
        Optional<Video> findVideo = videoRepository.findById(videoId);
        findVideo.orElseThrow(() -> new NoSuchElementException(StatusCode.VIDEO_NOT_FOUND));
        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), VideoInfoDto.from(findVideo.get())));
    }

    @Transactional
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
}

