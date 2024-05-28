package CodeIt.Ytrip.config;

import CodeIt.Ytrip.common.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    /**
     * 로그인 인증 인가를 위한 인터셉터
     * 개발 단계에서는 주석 처리
     * 추후 배포 단계에서 주석 해제 필요
     */
//    private final JwtInterceptor jwtInterceptor;
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(jwtInterceptor)
//                .addPathPatterns("/**")
//                .excludePathPatterns("/api/auth/**","/test/**");
//    }
}

