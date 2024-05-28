package CodeIt.Ytrip.video.service;

import CodeIt.Ytrip.video.domain.Video;
import CodeIt.Ytrip.video.repository.VideoRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class VideoServiceTest {

    @Autowired
    VideoRepository videoRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void 비디오_목록_조회() throws Exception {
        //given
        List<Video> findVidoes = videoRepository.findTop12ByOrderByLikeCountDesc();
        //when
        int size = findVidoes.size();
        //then
        Assertions.assertThat(size).isEqualTo(12);
    }

    @Test
    public void 비디오_상세_정보_조회() throws Exception {
        Video test = Video.builder()
                .title("Test Title")
                .content("Test content")
                .build();

        videoRepository.save(test);

        Optional<Video> findVideo = videoRepository.findById(test.getId());
        Assertions.assertThat(test.getId()).isEqualTo(findVideo.get().getId());
        Assertions.assertThat(test.getTitle()).isEqualTo(findVideo.get().getTitle());
        Assertions.assertThat(test.getContent()).isEqualTo(findVideo.get().getContent());

    }

}