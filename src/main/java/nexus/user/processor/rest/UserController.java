// nexus/user/processor/rest/UserController.java
package nexus.user.processor.rest;

import lombok.RequiredArgsConstructor;
import nexus.common.security.JwtTokenProvider;
import nexus.user.application.UserQueryService;
import nexus.user.application.UserService;
import nexus.user.domain.User;
import nexus.user.domain.type.AgentStatus;
import nexus.user.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserQueryService userQueryService;
    private final JwtTokenProvider jwtTokenProvider; // ✅ 추가

    /**
     * 로그인 API (JWT 발급 포함)
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @RequestBody LoginRequest request
    ) {
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

    /**
     * 현재 사용자 프로필 조회
     */
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getCurrentUserProfile(Authentication authentication) {
        // ✅ null 체크 추가
        if (authentication == null) {
            return ResponseEntity.badRequest().build();
        }

        String email = authentication.getName();
        UserDto userProfile = userQueryService.getUserByEmail(email);
        return ResponseEntity.ok(userProfile);
    }

//    /**
//     * 프로필 업데이트
//     */
//    @PutMapping("/profile")
//    public ResponseEntity<UserDto> updateProfile(
//            @RequestBody UpdateProfileRequest request,
//            Authentication authentication) {
//
//        // ✅ null 체크 추가
//        if (authentication == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        String email = authentication.getName();
//        UserDto updatedUser = userService.updateProfile(email, request);
//        return ResponseEntity.ok(updatedUser);
//    }
//
//    /**
//     * 상담원 상태 변경 (인증 기반)
//     */
//    @PutMapping("/status")
//    public ResponseEntity<UserDto> updateCallStatus(
//            @RequestParam AgentStatus callStatus,
//            Authentication authentication) {
//
//        // ✅ null 체크 추가
//        if (authentication == null) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        String email = authentication.getName();
//        UpdateProfileRequest request = new UpdateProfileRequest();
//        request.setCallStatus(callStatus);
//
//        UserDto updatedUser = userService.updateProfile(email, request);
//        return ResponseEntity.ok(updatedUser);
//    }

    /**
     * 프로필 업데이트
     */
    @PutMapping("/profile")
    public ResponseEntity<UserDto> updateProfile(
            @RequestBody UpdateProfileRequest request,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Bearer 토큰에서 실제 토큰 추출
            String token = jwtTokenProvider.extractTokenFromBearer(authHeader);

            // 토큰 유효성 검증
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // JWT에서 이메일 추출
            String email = jwtTokenProvider.getEmailFromToken(token);

            UserDto updatedUser = userService.updateProfile(email, request);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 상담원 상태 변경 (인증 기반)
     */
    @PutMapping("/status")
    public ResponseEntity<UserDto> updateCallStatus(
            @RequestParam AgentStatus callStatus,
            @RequestHeader("Authorization") String authHeader) {

        try {
            // Bearer 토큰에서 실제 토큰 추출
            String token = jwtTokenProvider.extractTokenFromBearer(authHeader);

            // 토큰 유효성 검증
            if (token == null || !jwtTokenProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // JWT에서 이메일 추출
            String email = jwtTokenProvider.getEmailFromToken(token);

            UpdateProfileRequest request = new UpdateProfileRequest();
            request.setCallStatus(callStatus);

            UserDto updatedUser = userService.updateProfile(email, request);
            return ResponseEntity.ok(updatedUser);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * 전체 사용자 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userQueryService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 특정 사용자 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userQueryService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * ✅ 상담원 상태 변경 (ID 기반 - 개발용)
     */
    @PutMapping("/{userId}/status")
    public ResponseEntity<UserDto> updateCallStatusById(
            @PathVariable Long userId,
            @RequestParam AgentStatus callStatus) {

        UserDto updatedUser = userService.updateProfileById(userId, callStatus);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * ✅ 사용자 검색
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String keyword) {
        List<UserDto> users = userQueryService.findUsersByKeyword(keyword);
        return ResponseEntity.ok(users);
    }


}