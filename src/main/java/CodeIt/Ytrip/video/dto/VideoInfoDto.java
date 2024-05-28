package CodeIt.Ytrip.video.dto;

import CodeIt.Ytrip.video.domain.Video;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder
public class VideoInfoDto {
    private Long id;
    private String title;
    private String content;
    private String url;
    private String tag;
    private Integer likeCount;

    public static VideoInfoDto from(Video video) {
        return VideoInfoDto.builder()
                .id(video.getId())
                .title(video.getTitle())
                .content(video.getContent())
                .url(video.getUrl())
                .tag(video.getTag())
                .likeCount(video.getLikeCount())
                .build();
    }
}
