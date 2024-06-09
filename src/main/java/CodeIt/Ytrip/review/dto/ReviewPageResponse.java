package CodeIt.Ytrip.review.dto;

import CodeIt.Ytrip.common.dto.BasePageDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewPageResponse {
    private List<ReviewDto> content;
    private BasePageDto page_info;

    public static ReviewPageResponse of(List<ReviewDto> content, BasePageDto basePageDto) {
        return ReviewPageResponse.builder()
                .content(content)
                .page_info(basePageDto)
                .build();
    }
}
