// nexus/user/dto/UpdateProfileRequest.java
package nexus.user.dto;

import lombok.Data;
import nexus.user.domain.type.AgentStatus;

@Data
public class UpdateProfileRequest {
    private String name;
    private String email;
    private String profileImage;
    private AgentStatus callStatus; // ✅ 상담원 상태 변경 가능
}