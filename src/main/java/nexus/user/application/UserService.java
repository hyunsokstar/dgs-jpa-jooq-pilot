package nexus.user.application;

import lombok.RequiredArgsConstructor;
import nexus.user.domain.User;
import nexus.user.dto.UserDto;
import nexus.user.infrastructure.JpaUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JpaUserRepository userRepository;

    public User register(String email, String name, String password) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        return userRepository.save(User.builder()
                .email(email)
                .name(name)
                .password(password) // 실제 서비스에선 반드시 암호화
                .build());
    }

//    public List<UserDto> findAll() {
//        return userRepository.findAll().stream()
//                .map(user -> {
//                    UserDto dto = new UserDto();
//                    dto.setId(user.getId());
//                    dto.setEmail(user.getEmail());
//                    dto.setName(user.getName());
//                    return dto;
//                }).toList();
//    }
}
