// 📄 nexus/user/presentation/rest/UserController.java

package nexus.user.presentation.rest;

import lombok.RequiredArgsConstructor;
import nexus.user.application.UserService;
import nexus.user.domain.User;
import nexus.user.dto.LoginRequest;     // ✅ 로그인 요청 DTO import
import nexus.user.dto.LoginResponse;    // ✅ 로그인 응답 DTO import
import nexus.user.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 로그인 API (JWT 발급 포함)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody LoginRequest request // ✅ @RequestBody로 JSON 요청 바인딩 받음
    ) {
        // ✅ 요청 객체에서 이메일/비밀번호 추출하여 서비스에 전달
        LoginResponse loginResponse = userService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(loginResponse);
    }

    /**
     * 회원 가입 API
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @RequestBody RegisterRequest request
    ) {
        User registeredUser = userService.register(request.getEmail(), request.getName(), request.getPassword());
        return ResponseEntity.ok(registeredUser);
    }
}
