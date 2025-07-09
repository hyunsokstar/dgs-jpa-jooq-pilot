package nexus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import nexus.user.domain.type.AgentStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "agent_task_status")
public class AgentTaskStatus {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_status", nullable = false)
    private AgentStatus taskStatus;
}
