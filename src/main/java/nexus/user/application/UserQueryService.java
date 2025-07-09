// nexus/user/application/UserQueryService.java
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
     * 모든 사용자 조회 (모든 필드 포함)
     */
    public List<UserDto> getAllUsers() {
        return dsl
                .select(
                        USERS.ID,
                        USERS.EMAIL,
                        USERS.NAME,
                        USERS.PROFILE_IMAGE,     // ✅ 추가
                        USERS.CALL_STATUS,       // ✅ 추가
                        USERS.CREATED_AT         // ✅ 추가
                )
                .from(USERS)
                .fetchInto(UserDto.class);
    }

    /**
     * ID로 사용자 단건 조회 (모든 필드 포함)
     */
    public UserDto getUserById(Long id) {
        return dsl
                .select(
                        USERS.ID,
                        USERS.EMAIL,
                        USERS.NAME,
                        USERS.PROFILE_IMAGE,     // ✅ 추가
                        USERS.CALL_STATUS,       // ✅ 추가
                        USERS.CREATED_AT         // ✅ 추가
                )
                .from(USERS)
                .where(USERS.ID.eq(id))
                .fetchOneInto(UserDto.class);
    }

    /**
     * 이메일로 사용자 단건 조회 (모든 필드 포함)
     */
    public UserDto getUserByEmail(String email) {
        return dsl
                .select(
                        USERS.ID,
                        USERS.EMAIL,
                        USERS.NAME,
                        USERS.PROFILE_IMAGE,     // ✅ 추가
                        USERS.CALL_STATUS,       // ✅ 추가
                        USERS.CREATED_AT         // ✅ 추가
                )
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOneInto(UserDto.class);
    }

    /**
     * 키워드로 사용자 검색 (모든 필드 포함)
     */
    public List<UserDto> findUsersByKeyword(String keyword) {
        return dsl
                .select(
                        USERS.ID,
                        USERS.EMAIL,
                        USERS.NAME,
                        USERS.PROFILE_IMAGE,     // ✅ 추가
                        USERS.CALL_STATUS,       // ✅ 추가
                        USERS.CREATED_AT         // ✅ 추가
                )
                .from(USERS)
                .where(USERS.NAME.containsIgnoreCase(keyword)
                        .or(USERS.EMAIL.containsIgnoreCase(keyword)))
                .fetchInto(UserDto.class);
    }

    /**
     * 상담원 상태별 사용자 조회 (모든 필드 포함)
     */
    public List<UserDto> getUsersByCallStatus(String callStatus) {
        return dsl
                .select(
                        USERS.ID,
                        USERS.EMAIL,
                        USERS.NAME,
                        USERS.PROFILE_IMAGE,     // ✅ 추가
                        USERS.CALL_STATUS,       // ✅ 추가
                        USERS.CREATED_AT         // ✅ 추가
                )
                .from(USERS)
                .where(USERS.CALL_STATUS.eq(callStatus))
                .fetchInto(UserDto.class);
    }
}