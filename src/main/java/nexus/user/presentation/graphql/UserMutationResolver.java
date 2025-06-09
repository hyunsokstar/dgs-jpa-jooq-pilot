package nexus.user.presentation.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import nexus.user.application.UserService;
import nexus.user.domain.User;
import nexus.user.dto.LoginResponse;

@DgsComponent
@RequiredArgsConstructor
public class UserMutationResolver {

    private final UserService userService;

    // ✅ 로그인 Mutation 추가
    @DgsMutation
    public LoginResponse loginUser(
            @InputArgument(name = "email") String email,
            @InputArgument(name = "password") String password
    ) {
        return userService.login(email, password);
    }

    @DgsMutation
    public User registerUser(
            @InputArgument(name = "email") String email,
            @InputArgument(name = "name") String name,
            @InputArgument(name = "password") String password
    ) {
        return userService.register(email, name, password);
    }

}
