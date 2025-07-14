package nexus.user.processor.rest;

import lombok.RequiredArgsConstructor;
import nexus.user.infrastructure.scheduler.AgentStatusScheduler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/personal-app-info")
@RequiredArgsConstructor
public class AgentStatusController {

    private final AgentStatusScheduler agentStatusScheduler;

    /**
     * 스케줄러 활성화
     */
    @PostMapping("/scheduler/enable")
    public ResponseEntity<Map<String, Object>> enableScheduler() {
        agentStatusScheduler.enableScheduler();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "enabled", true,
                "message", "Agent status scheduler enabled",
                "timestamp", System.currentTimeMillis()
        ));
    }

    /**
     * 스케줄러 비활성화
     */
    @PostMapping("/scheduler/disable")
    public ResponseEntity<Map<String, Object>> disableScheduler() {
        agentStatusScheduler.disableScheduler();
        return ResponseEntity.ok(Map.of(
                "success", true,
                "enabled", false,
                "message", "Agent status scheduler disabled",
                "timestamp", System.currentTimeMillis()
        ));
    }

    /**
     * 스케줄러 상태 조회
     */
    @GetMapping("/scheduler/status")
    public ResponseEntity<Map<String, Object>> getSchedulerStatus() {
        return ResponseEntity.ok(Map.of(
                "enabled", agentStatusScheduler.isEnabled(),
                "interval", "3 seconds",
                "timestamp", System.currentTimeMillis()
        ));
    }

}
