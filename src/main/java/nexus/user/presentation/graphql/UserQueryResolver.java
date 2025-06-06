// 📄 src/main/java/nexus/user/presentation/graphql/UserQueryResolver.java
package nexus.user.presentation.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import nexus.user.application.UserQueryService;
import nexus.user.dto.UserDto;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class UserQueryResolver {

    private final UserQueryService userQueryService;

    @DgsQuery
    public String hello() {
        return "Hello from DGS!";
    }

    @DgsQuery
    public List<UserDto> allUsers() {
        return userQueryService.allUsers(); // ✅ 내부 위임
    }

    @DgsQuery
    public List<UserDto> findUsersByKeyword(@InputArgument String keyword) {
        return userQueryService.findByKeyword(keyword); // ✅ 검색
    }
}
