package nexus.user.application;

import lombok.RequiredArgsConstructor;
import nexus.user.dto.UserDto;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import static nexus.jooq.generated.tables.Users.USERS;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final DSLContext dsl;

    /**
     * 모든 사용자 조회
     */
    public List<UserDto> getAllUsers() {
        return dsl
                .select(USERS.ID, USERS.EMAIL, USERS.NAME)
                .from(USERS)
                .fetchInto(UserDto.class); // 자동 매핑
    }

    /**
     * ID로 사용자 단건 조회
     */
    public UserDto getUserById(Long id) {
        return dsl
                .select(USERS.ID, USERS.EMAIL, USERS.NAME)
                .from(USERS)
                .where(USERS.ID.eq(id))
                .fetchOneInto(UserDto.class); // 자동 매핑
    }
}
