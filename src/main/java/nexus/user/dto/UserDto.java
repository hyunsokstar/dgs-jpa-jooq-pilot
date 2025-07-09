// nexus/user/dto/UserDto.java
package nexus.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nexus.user.domain.User;
import nexus.user.domain.type.AgentStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
    private String profileImage;
    private AgentStatus callStatus; // ✅ 추가
    private LocalDateTime createdAt;

    // ✅ Entity -> DTO 변환 메서드
    public static UserDto from(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .profileImage(user.getProfileImage())
                .callStatus(user.getCallStatus()) // ✅ 추가
                .createdAt(user.getCreatedAt())
                .build();
    }
}