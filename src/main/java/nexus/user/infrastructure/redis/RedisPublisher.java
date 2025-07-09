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

    public void publishUserProfileUpdate(Long userId, String field, Object newValue) {
        String channel = "user:profile:updated";
        String message = String.format(
                "{\"userId\":%d,\"field\":\"%s\",\"newValue\":\"%s\",\"timestamp\":%d}",
                userId, field, newValue, System.currentTimeMillis()
        );

        stringRedisTemplate.convertAndSend(channel, message);
        log.info("Published to Redis: {}", message);
    }
}