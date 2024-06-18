package CodeIt.Ytrip.video.dto;

import CodeIt.Ytrip.video.domain.Video;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Data
@Getter
@Builder
public class VideoListDto {

    private Long id;
    private String title;
    private String videoUrl;
    private String imageUrl;
    private List<String> tags;

    public static VideoListDto from(Video video) {
        return VideoListDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .videoUrl(video.getVideoUrl())
                .imageUrl(video.getImageUrl())
                .tags(video.getTags())
                .build();
    }
}
