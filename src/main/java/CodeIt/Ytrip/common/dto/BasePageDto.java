package CodeIt.Ytrip.common.dto;

import CodeIt.Ytrip.review.dto.ReviewDto;
import CodeIt.Ytrip.video.dto.VideoListDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class BasePageDto<T> {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isFirst;
    private boolean isLast;

    public static <T> BasePageDto<T> from(Page<T> page) {
        return BasePageDto.<T>builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .isFirst(page.isFirst())
                .isLast(page.isLast())
                .build();
    }
}
