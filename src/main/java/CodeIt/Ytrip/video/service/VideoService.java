package CodeIt.Ytrip.video.service;

import CodeIt.Ytrip.common.exception.NoSuchElementException;
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
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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

//    public ResponseEntity<?> likeVideo(Long videoId) {
//        // 1. 영상 찾기
//        Video video = videoRepository.findById(videoId)
//                .orElseThrow(() -> new NoSuchElementException(StatusCode.VIDEO_NOT_FOUND.getMessage()));
//
//        // 2. 현재 인증된 사용자 정보 가져오기
//        User user = getCurrentAuthenticatedUser();
//
//        // 3. 이미 좋아요를 눌렀는지 확인
//        Optional<VideoLike> existingLike = videoLikeRepository.findByUserAndVideo(user, video);
//        if (existingLike.isPresent()) {
//            // 이미 좋아요가 있으면 삭제
//            videoLikeRepository.deleteByUserAndVideo(user, video);
//            video.decrementLikeCount();
//            videoRepository.save(video);
//            return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), "Like removed successfully"));
//        }
//
//        // 4. 좋아요 처리 로직
//        VideoLike newLike = VideoLike.builder()
//                .user(user)
//                .video(video)
//                .build();
//        videoLikeRepository.save(newLike);
//
//        // 5. 좋아요 수 업데이트
//        video.incrementLikeCount();
//        videoRepository.save(video);
//
//        return ResponseEntity.ok(SuccessResponse.of(StatusCode.SUCCESS.getCode(), StatusCode.SUCCESS.getMessage(), "Liked successfully"));
//    }
//
//    private User getCurrentAuthenticatedUser() {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//    }
}
