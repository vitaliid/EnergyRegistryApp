package org.example.domain.logging;

import java.time.Instant;
import java.util.Map;

public record UserActionEvent(
        UserActionType action,
        UserActionOutcome outcome,
        String targetType,
        String targetId,
        Map<String, Object> metadata,
        String actorId,
        String actorName,
        String requestId,
        String httpMethod,
        String requestPath,
        String remoteAddress,
        String userAgent,
        Instant occurredAt
) {
    public UserActionEvent {
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
        occurredAt = occurredAt == null ? Instant.now() : occurredAt;
    }
}