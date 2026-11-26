package org.sopt;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE");

        // 쿠키로 인증하는 재발급과 로그아웃은 같은 오리진에서만 허용한다.
        registry.addMapping("/auth/reissue")
                .allowedOrigins();
        registry.addMapping("/auth/logout")
                .allowedOrigins();
    }
}
