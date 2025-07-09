package nexus.user.processor.graphql;

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
    public List<UserDto> getAllUsers() {
        return userQueryService.getAllUsers();
    }

    @DgsQuery
    public UserDto getUserById(@InputArgument Long id) {
        return userQueryService.getUserById(id);
    }
}
