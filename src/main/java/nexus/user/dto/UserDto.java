// 📄 src/main/java/nexus/user/dto/UserDto.java
package nexus.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
}
