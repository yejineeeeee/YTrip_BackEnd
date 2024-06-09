package CodeIt.Ytrip.video.dto;

import CodeIt.Ytrip.video.domain.Video;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Builder
public class VideoInfoDto {
    private Long id;
    private String title;
    private String content;
    private String url;
    private List<String> tags;
    private int likeCount;
    private LocalDateTime createdAt;


    public static VideoInfoDto from(Video video) {
        return VideoInfoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .content(video.getContent())
                .url(video.getUrl())
                .tags(video.getTags())
                .likeCount(video.getLikeCount())
                .createdAt(video.getCreatedAt())
                .build();
    }
}
