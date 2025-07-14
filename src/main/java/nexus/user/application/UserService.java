package nexus.user.application;

import lombok.RequiredArgsConstructor;
import nexus.common.security.JwtTokenProvider;
import nexus.user.domain.User;
import nexus.user.domain.type.AgentStatus; // ✅ import 추가
import nexus.user.dto.LoginResponse;
import nexus.user.dto.UpdateProfileRequest;
import nexus.user.dto.UserDto;
import nexus.user.infrastructure.JpaUserRepository;
import nexus.user.infrastructure.redis.RedisPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional // ✅ 트랜잭션 추가
public class UserService {

    private final JpaUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // private final RedisPublisher redisPublisher; // ✅ 2단계에서 추가할 예정
    private final RedisPublisher redisPublisher; // 추가


    public LoginResponse login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = jwtTokenProvider.createToken(email);
        return new LoginResponse(token,user.getId(), user.getEmail(), user.getName());
    }

    public User register(String email, String name, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(password);
        return userRepository.save(User.builder()
                .email(email)
                .name(name)
                .password(encodedPassword)
                .build());
    }

    /**
     * 이메일 기반 프로필 업데이트
     */
    public UserDto updateProfile(String email, UpdateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 필드별 업데이트 및 Redis pub
        if (request.getName() != null && !request.getName().equals(user.getName())) {
            user.updateName(request.getName());
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

        // 상담원 상태 업데이트
        if (request.getCallStatus() != null && !request.getCallStatus().equals(user.getCallStatus())) {
            user.updateCallStatus(request.getCallStatus());
            // redisPublisher.publishUserProfileUpdate(user.getId(), "callStatus", request.getCallStatus());
        }

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

    /**
     * ✅ ID 기반 상담원 상태 변경 (개발용)
     */
    public UserDto updateProfileById(Long userId, AgentStatus callStatus) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 상태 업데이트
        user.updateCallStatus(callStatus);
        User savedUser = userRepository.save(user);

        // ✅ Redis pub 추가
        redisPublisher.publishUserProfileUpdate(userId, "callStatus", callStatus);

        return UserDto.from(savedUser);
    }

    /**
     * ✅ 비밀번호 변경
     */
    public void changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(encodedNewPassword);
        userRepository.save(user);

        // ✅ 2단계: Redis pub 추가 예정
        // redisPublisher.publishUserProfileUpdate(user.getId(), "password", "changed");
    }

    /**
     * ✅ ID 기반 전체 프로필 업데이트 (확장용)
     */
    public UserDto updateProfileById(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 필드별 업데이트
        if (request.getName() != null && !request.getName().equals(user.getName())) {
            user.updateName(request.getName());
            // redisPublisher.publishUserProfileUpdate(userId, "name", request.getName());
        }

        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
            }
            user.updateEmail(request.getEmail());
            // redisPublisher.publishUserProfileUpdate(userId, "email", request.getEmail());
        }

        if (request.getProfileImage() != null) {
            user.updateProfileImage(request.getProfileImage());
            // redisPublisher.publishUserProfileUpdate(userId, "profileImage", request.getProfileImage());
        }

        if (request.getCallStatus() != null && !request.getCallStatus().equals(user.getCallStatus())) {
            user.updateCallStatus(request.getCallStatus());
            // redisPublisher.publishUserProfileUpdate(userId, "callStatus", request.getCallStatus());
        }

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }
}