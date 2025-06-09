package nexus.user.application;

import lombok.RequiredArgsConstructor;
import nexus.user.dto.UserDto;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import java.util.List;

import nexus.jooq.generated.tables.Users;
@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final DSLContext dsl;

    public List<UserDto> allUsers() {
        return dsl.selectFrom(Users.USERS)
                .fetchInto(UserDto.class);
    }

    /**
     * 이름 또는 이메일에 키워드가 포함된 사용자 검색
     */
    public List<UserDto> findByKeyword(String keyword) {
        return dsl.selectFrom(Users.USERS)
                .where(
                        Users.USERS.NAME.containsIgnoreCase(keyword)
                                .or(Users.USERS.EMAIL.containsIgnoreCase(keyword))
                )
                .fetchInto(UserDto.class);
    }
}
