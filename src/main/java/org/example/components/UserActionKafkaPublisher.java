package org.example.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.logging.UserActionEvent;
import org.example.domain.logging.UserActionOutcome;
import org.example.domain.logging.UserActionType;
import org.example.filter.RequestIdFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionKafkaPublisher {

    private final KafkaTemplate<String, UserActionEvent> kafkaTemplate;

    @Value("${app.kafka.topics.user-actions}")
    private String topic;

    public void success(
            UserActionType action,
            String targetType,
            Object targetId,
            Map<String, Object> metadata
    ) {
        UserActionEvent event = buildEvent(
                action,
                UserActionOutcome.SUCCESS,
                targetType,
                targetId,
                metadata
        );

        sendAfterCommit(event);
    }

    public void failure(
            UserActionType action,
            String targetType,
            Object targetId,
            String failureCode
    ) {
        UserActionEvent event = buildEvent(
                action,
                UserActionOutcome.FAILURE,
                targetType,
                targetId,
                Map.of("failureCode", failureCode)
        );

        sendAfterRollback(event);
    }

    private void sendAfterCommit(UserActionEvent event) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            send(event);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        send(event);
                    }
                }
        );
    }

    private void sendAfterRollback(UserActionEvent event) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            send(event);
            return;
        }

        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCompletion(int status) {
                        if (status == TransactionSynchronization.STATUS_ROLLED_BACK) {
                            send(event);
                        }
                    }
                }
        );
    }

    private void send(UserActionEvent event) {
        String key = event.targetType() + ":"
                + (event.targetId() != null ? event.targetId() : event.requestId());

        try {
            kafkaTemplate.executeInTransaction(operations -> {
                operations.send(topic, key, event);
                return null;
            });
        } catch (RuntimeException ex) {
            log.error(
                    "Could not publish user action to Kafka. action={}, targetType={}, targetId={}",
                    event.action(),
                    event.targetType(),
                    event.targetId(),
                    ex
            );
        }
    }

    private UserActionEvent buildEvent(
            UserActionType action,
            UserActionOutcome outcome,
            String targetType,
            Object targetId,
            Map<String, Object> metadata
    ) {
        HttpServletRequest request = currentRequest();
        Actor actor = currentActor(request != null);

        return new UserActionEvent(
                action,
                outcome,
                targetType,
                targetId == null ? null : targetId.toString(),
                metadata == null ? Map.of() : Map.copyOf(metadata),
                actor.id(),
                actor.name(),
                request == null ? null : String.valueOf(
                        request.getAttribute(RequestIdFilter.ATTRIBUTE)
                ),
                request == null ? null : request.getMethod(),
                request == null ? null : request.getRequestURI(),
                request == null ? null : request.getRemoteAddr(),
                request == null ? null : request.getHeader("User-Agent"),
                Instant.now()
        );
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }
        return null;
    }

    private Actor currentActor(boolean httpRequestExists) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null
                || !auth.isAuthenticated()
                || auth instanceof AnonymousAuthenticationToken) {
            return new Actor(httpRequestExists ? "ANONYMOUS" : "SYSTEM", null);
        }

        if (auth.getPrincipal() instanceof Jwt jwt) {
            String name = jwt.getClaimAsString("preferred_username");
            return new Actor(
                    jwt.getSubject() != null ? jwt.getSubject() : auth.getName(),
                    name
            );
        }

        return new Actor(auth.getName(), null);
    }

    private record Actor(String id, String name) {
    }
}