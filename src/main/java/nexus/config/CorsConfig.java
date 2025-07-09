// src/main/java/nexus/config/CorsConfig.java
package nexus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**") // ✅ /api/** 경로에 대해
                .allowedOrigins(
                        "http://localhost:1420",  // ✅ Tauri 개발 서버
                        "http://localhost:3000",  // ✅ React 개발 서버
                        "http://127.0.0.1:1420",
                        "tauri://localhost"       // ✅ Tauri 프로덕션
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // ✅ 허용할 HTTP 메서드
                .allowedHeaders("*") // ✅ 모든 헤더 허용
                .allowCredentials(true) // ✅ 쿠키/인증 정보 허용
                .maxAge(3600); // ✅ Preflight 캐시 1시간
    }

    // ✅ Spring Security와 함께 사용할 경우를 위한 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:1420");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://127.0.0.1:1420");
        configuration.addAllowedOrigin("tauri://localhost");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}