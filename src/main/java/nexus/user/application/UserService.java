package nexus.user.application;

import lombok.RequiredArgsConstructor;
import nexus.common.security.JwtTokenProvider; // ✅ JWT 생성 유틸
import nexus.user.domain.User;
import nexus.user.dto.LoginResponse;
import nexus.user.infrastructure.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider; // ✅ JWT 토큰 프로바이더 의존성 주입

    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email) // ✅ 이메일로 사용자 조회
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) { // ✅ 비밀번호 검증
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(email); // ✅ JWT 토큰 생성

        return new LoginResponse(token, user.getEmail(), user.getName()); // ✅ 토큰 + 사용자 정보 반환
    }

    public User register(String email, String name, String password) {
        if (userRepository.existsByEmail(email)) { // ✅ 이메일 중복 체크
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password); // ✅ 비밀번호 암호화

        return userRepository.save(User.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .build()); // ✅ 사용자 저장
    }

}
