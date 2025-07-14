// src/main/java/nexus/user/infrastructure/scheduler/AgentStatusScheduler.java
package nexus.user.infrastructure.scheduler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nexus.user.application.UserQueryService;
import nexus.user.dto.AgentStatusPayload;
import nexus.user.dto.UserDto;
import nexus.user.infrastructure.redis.RedisPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgentStatusScheduler {

    private final UserQueryService userQueryService;
    private final RedisPublisher redisPublisher;
    private final ObjectMapper objectMapper;
    private final Random random = new Random();
    private final String CHANNEL_NAME = "agent:call-status:info";


    @Scheduled(fixedRate = 5000)
    public void publishAllAgentStatusInfo() {
        try {
            String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            List<UserDto> allAgents = userQueryService.getAllUsers();

            for (UserDto agent : allAgents) {
                // 👉 Redis 채널 네이밍 변경
                String channel = "personal:agent-info:" + agent.getId();

                AgentStatusPayload payload = new AgentStatusPayload(
                        agent.getId(),
                        agent.getName(),
                        agent.getCallStatus().toString()  // 반드시 문자열화!

                        // ⬇️ 여기에 추가 정보 필요 시 아래처럼 확장 가능
                        // agent.getInboundStats()  // 인바운드 상담 건수
                        // agent.getOutboundStats() // 아웃바운드 상담 건수
                        // agent.getAccumulatedTalkTime() // 누적 상담 시간
                );

                String jsonMessage = objectMapper.writeValueAsString(payload);

                System.out.println("channel : " + channel);
                System.out.println("statusInfo : " + jsonMessage);
                System.out.println("current time : " + now);

                redisPublisher.publishMessage(channel, jsonMessage);
            }

            log.debug("✅ {} agents 상태 pub 완료 (5초 주기)", allAgents.size());

        } catch (Exception e) {
            log.error("🚨 상태 pub 중 오류 발생", e);
        }
    }

    /**
     * 스케줄러 제어용 메서드들
     */
    private volatile boolean enabled = true;

    public void enableScheduler() {
        this.enabled = true;
        log.info("Agent status scheduler enabled");
    }

    public void disableScheduler() {
        this.enabled = false;
        log.info("Agent status scheduler disabled");
    }

    public boolean isEnabled() {
        return enabled;
    }
}