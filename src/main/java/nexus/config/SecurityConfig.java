// 📄 nexus/config/SecurityConfig.java

package nexus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // ✅ PasswordEncoder 구현체 import 추가
import org.springframework.security.crypto.password.PasswordEncoder;   // ✅ PasswordEncoder 인터페이스 import 추가
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // ✅ CSRF 보호 비활성화 (람다 방식 사용)

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new AntPathRequestMatcher("/graphql")).permitAll() // ✅ GraphQL 허용
                        .requestMatchers(new AntPathRequestMatcher("/api/users/login")).permitAll() // ✅ REST 로그인 허용
                        .requestMatchers(new AntPathRequestMatcher("/api/users/register")).permitAll() // ✅ REST 회원가입 허용
                        .anyRequest().authenticated() // ✅ 나머지 경로는 인증 필요
                );

        return http.build(); // ✅ SecurityFilterChain 생성
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // ✅ PasswordEncoder Bean 등록
    }
}
