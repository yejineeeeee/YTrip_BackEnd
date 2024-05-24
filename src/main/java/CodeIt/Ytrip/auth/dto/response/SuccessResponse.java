package CodeIt.Ytrip.auth.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SuccessResponse {
    private String status;
    private String message;
}
