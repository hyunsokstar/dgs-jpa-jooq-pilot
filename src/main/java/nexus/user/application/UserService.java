package nexus.user.application;

import lombok.RequiredArgsConstructor;
import nexus.common.security.JwtTokenProvider; // ✅ JWT 생성 유틸
import nexus.user.domain.User;
import nexus.user.dto.LoginResponse;
import nexus.user.dto.UpdateProfileRequest;
import nexus.user.dto.UserDto;
import nexus.user.infrastructure.JpaUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    // nexus/user/application/UserService.java의 updateProfile 메서드
    public UserDto updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 필드별 업데이트 및 Redis pub
        if (request.getName() != null && !request.getName().equals(user.getName())) {
            user.updateName(request.getName());
            // Redis pub (선택사항)
            // redisPublisher.publishUserProfileUpdate(user.getId(), "name", request.getName());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            // 이메일 중복 체크
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
            user.updateEmail(request.getEmail());
            // redisPublisher.publishUserProfileUpdate(user.getId(), "email", request.getEmail());
        }

        if (request.getProfileImage() != null) {
            user.updateProfileImage(request.getProfileImage());
            // redisPublisher.publishUserProfileUpdate(user.getId(), "profileImage", request.getProfileImage());
        }

        // ✅ 상담원 상태 업데이트 추가
        if (request.getCallStatus() != null && !request.getCallStatus().equals(user.getCallStatus())) {
            user.updateCallStatus(request.getCallStatus());
            // redisPublisher.publishUserProfileUpdate(user.getId(), "callStatus", request.getCallStatus());
        }

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

}
