package CodeIt.Ytrip.common.interceptor;

import CodeIt.Ytrip.common.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = jwtUtils.splitBearerToken(request.getHeader("Authorization"));
        if (jwtUtils.isValidToken(token)) {
            log.info("AccessToken = {}", token);
            return true;
        }
        Map<String, String> body = new HashMap<>();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json; charset=UTF-8");
        body.put("status", "0001");
        body.put("message","다시 로그인을 해주세요.");
        String authErrorResponse = objectMapper.writeValueAsString(body);
        response.getWriter().write(authErrorResponse);
        return false;
    }


}
