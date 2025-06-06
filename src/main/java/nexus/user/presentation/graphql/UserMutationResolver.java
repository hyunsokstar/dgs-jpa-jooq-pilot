package nexus.user.presentation.graphql;

import com.netflix.graphql.dgs.*;
import lombok.RequiredArgsConstructor;
import nexus.user.application.UserService;
import nexus.user.domain.User;

@DgsComponent
@RequiredArgsConstructor
public class UserMutationResolver {

    private final UserService userService;

    @DgsMutation
    public User registerUser(
            @InputArgument(name = "email") String email,
            @InputArgument(name = "name") String name,
            @InputArgument(name = "password") String password
    ) {
        return userService.register(email, name, password);
    }
}
