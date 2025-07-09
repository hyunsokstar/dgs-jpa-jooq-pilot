package nexus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import nexus.user.domain.type.AgentStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    // ✅ 프로필 이미지 추가
    private String profileImage;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "call_status")
    private AgentStatus callStatus = AgentStatus.READY;

    // ✅ 생성일시 추가
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // ✅ 업데이트 메서드들 (Setter 대신 도메인 로직)
    public void updateName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            this.name = name;
        }
    }

    public void updateEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            this.email = email;
        }
    }

    public void updatePassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            this.password = password;
        }
    }

    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void updateCallStatus(AgentStatus callStatus) {
        if (callStatus != null) {
            this.callStatus = callStatus;
        }
    }
}