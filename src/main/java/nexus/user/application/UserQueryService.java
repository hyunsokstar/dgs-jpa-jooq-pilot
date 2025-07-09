// nexus/user/application/UserQueryService.java
package nexus.user.application;

import lombok.RequiredArgsConstructor;
import nexus.user.dto.UserDto;
import org.jooq.DSLContext;
import org.jooq.SelectFieldOrAsterisk;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static nexus.jooq.generated.tables.Users.USERS;
import static org.jooq.impl.DSL.count; // ✅ DSL.count import 추가

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserQueryService {

    private final DSLContext dsl;

    private static final SelectFieldOrAsterisk[] USER_FIELDS = {
            USERS.ID,
            USERS.EMAIL,
            USERS.NAME,
            USERS.PROFILE_IMAGE,
            USERS.CALL_STATUS,
            USERS.CREATED_AT
    };

    /**
     * 모든 사용자 조회
     */
    public List<UserDto> getAllUsers() {
        return dsl
                .select(USER_FIELDS)
                .from(USERS)
                .orderBy(USERS.CREATED_AT.desc())
                .fetchInto(UserDto.class);
    }

    /**
     * ID로 사용자 조회
     */
    public UserDto getUserById(Long id) {
        return dsl
                .select(USER_FIELDS)
                .from(USERS)
                .where(USERS.ID.eq(id))
                .fetchOneInto(UserDto.class);
    }

    /**
     * 이메일로 사용자 조회
     */
    public UserDto getUserByEmail(String email) {
        return dsl
                .select(USER_FIELDS)
                .from(USERS)
                .where(USERS.EMAIL.eq(email))
                .fetchOneInto(UserDto.class);
    }

    /**
     * 키워드로 사용자 검색
     */
    public List<UserDto> findUsersByKeyword(String keyword) {
        return dsl
                .select(USER_FIELDS)
                .from(USERS)
                .where(USERS.NAME.containsIgnoreCase(keyword)
                        .or(USERS.EMAIL.containsIgnoreCase(keyword)))
                .orderBy(USERS.NAME.asc())
                .fetchInto(UserDto.class);
    }

    /**
     * 상담원 상태별 사용자 조회
     */
    public List<UserDto> getUsersByCallStatus(String callStatus) {
        return dsl
                .select(USER_FIELDS)
                .from(USERS)
                .where(USERS.CALL_STATUS.eq(callStatus))
                .orderBy(USERS.NAME.asc())
                .fetchInto(UserDto.class);
    }

    /**
     * 활성 사용자 조회 (OFFLINE 제외)
     */
    public List<UserDto> getActiveUsers() {
        return dsl
                .select(USER_FIELDS)
                .from(USERS)
                .where(USERS.CALL_STATUS.ne("OFFLINE"))
                .orderBy(USERS.NAME.asc())
                .fetchInto(UserDto.class);
    }

    /**
     * ✅ 상태별 사용자 수 통계 (수정됨)
     */
//    public List<StatusCountDto> getUserCountByStatus() {
//        return dsl
//                .select(
//                        USERS.CALL_STATUS.as("callStatus"),
//                        count(USERS.ID).as("count") // ✅ DSL.count() 사용
//                )
//                .from(USERS)
//                .groupBy(USERS.CALL_STATUS)
//                .fetchInto(StatusCountDto.class);
//    }

    /**
     * ✅ 전체 사용자 수
     */
    public Long getTotalUserCount() {
        return dsl
                .selectCount()
                .from(USERS)
                .fetchOneInto(Long.class);
    }

    /**
     * ✅ 특정 상태의 사용자 수
     */
    public Long getUserCountByStatus(String callStatus) {
        return dsl
                .selectCount()
                .from(USERS)
                .where(USERS.CALL_STATUS.eq(callStatus))
                .fetchOneInto(Long.class);
    }
}