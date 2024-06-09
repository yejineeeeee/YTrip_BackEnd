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
    private String url;
    private List<String> tags;

    public static VideoListDto from(Video video) {
        return VideoListDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .url(video.getUrl())
                .tags(video.getTags())
                .build();
    }
}
