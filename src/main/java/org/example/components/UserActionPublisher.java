package org.example.components;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.domain.logging.UserActionEvent;
import org.example.domain.logging.UserActionOutcome;
import org.example.domain.logging.UserActionType;
import org.example.filter.RequestIdFilter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserActionPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void success(
            UserActionType action,
            String targetType,
            Object targetId,
            Map<String, Object> metadata
    ) {
        publish(
                action,
                UserActionOutcome.SUCCESS,
                targetType,
                targetId,
                metadata
        );
    }

    public void failure(
            UserActionType action,
            String targetType,
            Object targetId,
            String failureCode
    ) {
        Map<String, Object> details = cleanMetadata(Map.of());
        details.put(
                "failureCode",
                hasText(failureCode) ? failureCode : "UNEXPECTED_ERROR"
        );

        publish(
                action,
                UserActionOutcome.FAILURE,
                targetType,
                targetId,
                details
        );
    }

    private void publish(
            UserActionType action,
            UserActionOutcome outcome,
            String targetType,
            Object targetId,
            Map<String, Object> metadata
    ) {
        HttpServletRequest request = currentRequest();
        Actor actor = currentActor(request != null);

        UserActionEvent event = new UserActionEvent(
                action,
                outcome,
                targetType,
                targetId == null ? null : targetId.toString(),
                cleanMetadata(metadata),
                actor.id(),
                actor.name(),
                requestId(request),
                request == null ? null : request.getMethod(),
                request == null ? null : request.getRequestURI(),
                request == null ? null : request.getRemoteAddr(),
                request == null ? null : truncate(request.getHeader("User-Agent"), 512),
                Instant.now()
        );

        applicationEventPublisher.publishEvent(event);
    }

    private Actor currentActor(boolean httpRequestExists) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return new Actor(httpRequestExists ? "ANONYMOUS" : "SYSTEM", null);
        }

        String actorId = authentication.getName();
        String actorName = null;

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            actorId = firstNonBlank(jwt.getSubject(), authentication.getName());
            actorName = firstNonBlank(
                    jwt.getClaimAsString("preferred_username"),
                    jwt.getClaimAsString("name")
            );
        }

        return new Actor(
                hasText(actorId) ? actorId : "SYSTEM",
                actorName
        );
    }

    private HttpServletRequest currentRequest() {
        if (RequestContextHolder.getRequestAttributes()
                instanceof ServletRequestAttributes attributes) {
            return attributes.getRequest();
        }

        return null;
    }

    private String requestId(HttpServletRequest request) {
        if (request == null) {
            return null;
        }

        Object value = request.getAttribute(RequestIdFilter.ATTRIBUTE);
        return value == null ? null : value.toString();
    }

    private Map<String, Object> cleanMetadata(Map<String, Object> metadata) {
        Map<String, Object> result = metadata == null
                ? new LinkedHashMap<>()
                : new LinkedHashMap<>(metadata);

        result.entrySet().removeIf(entry ->
                entry.getKey() == null || entry.getValue() == null
        );

        return result;
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (hasText(value)) {
                return value;
            }
        }

        return null;
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }

        return value.substring(0, maxLength);
    }

    private record Actor(String id, String name) {
    }
}