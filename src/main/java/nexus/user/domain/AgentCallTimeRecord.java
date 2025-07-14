package nexus.user.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "agent_call_time_record")
public class AgentCallTimeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    // 기본 4개 상태 시간 (초 단위)
    @Builder.Default
    @Column(name = "ready_time_seconds")
    private Long readyTimeSeconds = 0L;

    @Builder.Default
    @Column(name = "call_time_seconds")
    private Long callTimeSeconds = 0L;

    @Builder.Default
    @Column(name = "after_call_time_seconds")
    private Long afterCallTimeSeconds = 0L;

    @Builder.Default
    @Column(name = "break_time_seconds")
    private Long breakTimeSeconds = 0L;

    // 인바운드/아웃바운드 시간 (초 단위)
    @Builder.Default
    @Column(name = "inbound_time_seconds")
    private Long inboundTimeSeconds = 0L;

    @Builder.Default
    @Column(name = "outbound_time_seconds")
    private Long outboundTimeSeconds = 0L;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // 시간 누적 메서드들
    public void addReadyTime(long seconds) {
        this.readyTimeSeconds += seconds;
    }

    public void addCallTime(long seconds) {
        this.callTimeSeconds += seconds;
    }

    public void addAfterCallTime(long seconds) {
        this.afterCallTimeSeconds += seconds;
    }

    public void addBreakTime(long seconds) {
        this.breakTimeSeconds += seconds;
    }

    public void addInboundTime(long seconds) {
        this.inboundTimeSeconds += seconds;
    }

    public void addOutboundTime(long seconds) {
        this.outboundTimeSeconds += seconds;
    }

    // 총 근무 시간 계산
    public long getTotalWorkTimeSeconds() {
        return readyTimeSeconds + callTimeSeconds + afterCallTimeSeconds;
    }

    // 총 통화 시간 계산 (인바운드 + 아웃바운드)
    public long getTotalCallTimeSeconds() {
        return inboundTimeSeconds + outboundTimeSeconds;
    }

    // 연관관계 편의 메서드
    public void setUser(User user) {
        this.user = user;
    }
}