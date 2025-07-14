// src/main/java/nexus/user/infrastructure/redis/RedisPublisher.java
package nexus.user.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 기존 사용자 프로필 업데이트 pub
     */
    public void publishUserProfileUpdate(Long userId, String field, Object newValue) {
        String channel = "user:profile:updated";
        String message = String.format(
                "{\"userId\":%d,\"field\":\"%s\",\"newValue\":\"%s\",\"timestamp\":%d}",
                userId, field, newValue, System.currentTimeMillis()
        );

        stringRedisTemplate.convertAndSend(channel, message);
        log.info("Published to Redis channel '{}': {}", channel, message);
    }

    /**
     * 일반적인 메시지 발행 (스케줄러에서 사용)
     * @param channel Redis 채널명
     * @param message 발행할 메시지 (JSON 문자열)
     */
    public void publishMessage(String channel, String message) {
        try {
            stringRedisTemplate.convertAndSend(channel, message);
            log.debug("Published to Redis channel '{}': message length = {} chars",
                    channel, message.length());
        } catch (Exception e) {
            log.error("Failed to publish message to channel '{}': {}", channel, e.getMessage());
        }
    }

    /**
     * 상담원 상태 정보 전용 발행 메서드
     * @param agentStatusInfo 상담원 상태 정보 JSON
     */
    public void publishAgentStatusInfo(String agentStatusInfo) {
        String channel = "agent:call-status:info";
        publishMessage(channel, agentStatusInfo);
    }

    /**
     * 개별 상담원 상태 변경 알림 (이벤트 기반)
     */
    public void publishAgentStatusChange(Long agentId, String oldStatus, String newStatus) {
        String channel = "agent:status:change";
        String message = String.format(
                "{\"agentId\":%d,\"oldStatus\":\"%s\",\"newStatus\":\"%s\",\"timestamp\":%d}",
                agentId, oldStatus, newStatus, System.currentTimeMillis()
        );

        publishMessage(channel, message);
    }

    /**
     * 시스템 알림 발행
     */
    public void publishSystemNotification(String notificationType, String message) {
        String channel = "system:notification";
        String notification = String.format(
                "{\"type\":\"%s\",\"message\":\"%s\",\"timestamp\":%d}",
                notificationType, message, System.currentTimeMillis()
        );

        publishMessage(channel, notification);
    }
}