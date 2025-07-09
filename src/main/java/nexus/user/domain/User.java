package nexus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import nexus.user.domain.type.AgentStatus;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;
    private String password;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "call_status")
    private AgentStatus callStatus = AgentStatus.READY;

}
