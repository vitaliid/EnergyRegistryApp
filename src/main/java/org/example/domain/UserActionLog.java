package org.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.logging.UserActionEvent;
import org.example.domain.logging.UserActionOutcome;
import org.example.domain.logging.UserActionType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Entity
@Table(name = "user_action_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserActionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "occurred_at", nullable = false)
    private Instant occurredAt;

    @Column(name = "actor_id", nullable = false)
    private String actorId;

    @Column(name = "actor_name")
    private String actorName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserActionType action;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserActionOutcome outcome;

    @Column(name = "target_type", nullable = false)
    private String targetType;

    @Column(name = "target_id")
    private String targetId;

    @Column(name = "request_id")
    private String requestId;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "request_path")
    private String requestPath;

    @Column(name = "remote_address")
    private String remoteAddress;

    @Column(name = "user_agent")
    private String userAgent;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    public static UserActionLog from(UserActionEvent event) {
        UserActionLog log = new UserActionLog();
        log.occurredAt = event.occurredAt();
        log.actorId = event.actorId();
        log.actorName = event.actorName();
        log.action = event.action();
        log.outcome = event.outcome();
        log.targetType = event.targetType();
        log.targetId = event.targetId();
        log.requestId = event.requestId();
        log.httpMethod = event.httpMethod();
        log.requestPath = event.requestPath();
        log.remoteAddress = event.remoteAddress();
        log.userAgent = event.userAgent();
        log.metadata = event.metadata();
        return log;
    }
}