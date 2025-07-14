package nexus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import nexus.user.domain.type.AgentStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    private String profileImage;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "call_status")
    private AgentStatus callStatus = AgentStatus.READY;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // 연관관계 - 시간 기록들
    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AgentCallTimeRecord> callTimeRecords = new ArrayList<>();

    // 업데이트 메서드들
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

    // 연관관계 편의 메서드
    public void addCallTimeRecord(AgentCallTimeRecord record) {
        callTimeRecords.add(record);
        record.setUser(this);
    }

    public void removeCallTimeRecord(AgentCallTimeRecord record) {
        callTimeRecords.remove(record);
        record.setUser(null);
    }
}