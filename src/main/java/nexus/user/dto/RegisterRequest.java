// 📄 nexus/user/dto/RegisterRequest.java

package nexus.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String email;   // 사용자 이메일
    private String name;    // 사용자 이름
    private String password; // 사용자 비밀번호 (평문으로 전송됨, 서버에서 암호화)
}
