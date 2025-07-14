package nexus.user.domain.type;

public enum AgentStatus {
    READY,      // 대기
    BUSY,       // 통화중
    AFTER_CALL, // 후처리
    BREAK       // 휴식
}
