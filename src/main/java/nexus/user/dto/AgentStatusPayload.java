package nexus.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgentStatusPayload {
    private Long agentId;
    private String name;
    private String callStatus;
}
