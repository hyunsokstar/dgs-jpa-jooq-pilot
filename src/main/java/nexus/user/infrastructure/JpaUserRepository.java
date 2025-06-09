// 📄 nexus/user/infrastructure/JpaUserRepository.java

package nexus.user.infrastructure;

import nexus.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<User, Long> {

    // ✅ 이메일 존재 여부만 확인할 때 사용 (회원가입 중복 체크 등)
    boolean existsByEmail(String email);

    // ✅ 이메일로 사용자 조회 (로그인 등에서 사용자 정보 불러올 때 사용)
    Optional<User> findByEmail(String email);
}
