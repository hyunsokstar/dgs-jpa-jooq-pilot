// 📄 src/main/java/nexus/user/application/UserQueryService.java
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
     * 필드 직접 접근 방식 (가장 확실함)
     */
    public List<UserDto> findByKeyword(String keyword) {
        return dsl.selectFrom(USERS)
                .where(
                        USERS.NAME.containsIgnoreCase(keyword)
                                .or(USERS.EMAIL.containsIgnoreCase(keyword))
                )
                .fetch()
                .map(record -> new UserDto(
                        record.get(USERS.ID),      // ✅ 1. id
                        record.get(USERS.EMAIL),   // ✅ 2. email (순서 수정!)
                        record.get(USERS.NAME)     // ✅ 3. name (순서 수정!)
                ));
    }

    /**
     * 모든 사용자 조회
     */
    public List<UserDto> allUsers() {
        return dsl.selectFrom(USERS)
                .fetch()
                .map(record -> new UserDto(
                        record.get(USERS.ID),      // ✅ 1. id
                        record.get(USERS.EMAIL),   // ✅ 2. email (순서 수정!)
                        record.get(USERS.NAME)     // ✅ 3. name (순서 수정!)
                ));
    }
}