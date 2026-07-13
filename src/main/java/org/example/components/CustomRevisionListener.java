package org.example.components;

import jakarta.servlet.http.HttpServletRequest;
import org.example.domain.CustomRevisionEntity;
import org.example.filter.RequestIdFilter;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.oauth2.jwt.Jwt;

public class CustomRevisionListener implements RevisionListener {

    private static final String APPLICATION = "energyregistry-app";
    private static final String SYSTEM = "SYSTEM";

    @Override
    public void newRevision(Object revisionEntity) {

        if (!(revisionEntity instanceof CustomRevisionEntity revision)) {
            throw new IllegalArgumentException(
                    "Expected CustomRevisionEntity, got: " + revisionEntity.getClass()
            );
        }

        HttpServletRequest request = currentRequest();
        revision.setUsername(currentUsername());
        revision.setApplication(APPLICATION);
        revision.setRequestId(requestId(request));
        revision.setRemoteAddress(remoteAddress(request));
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
            return null; // Background job / non-HTTP execution
        }

        Object requestId = request.getAttribute(RequestIdFilter.ATTRIBUTE);
        return requestId == null ? null : requestId.toString();
    }

    private String remoteAddress(HttpServletRequest request) {
        return request == null ? null : request.getRemoteAddr();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private String currentUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return SYSTEM;
        }

        // For Keycloak JWTs, prefer a readable login name.
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String preferredUsername = jwt.getClaimAsString("preferred_username");
            if (hasText(preferredUsername)) {
                return preferredUsername;
            }

            if (hasText(jwt.getSubject())) {
                return jwt.getSubject();
            }
        }

        return hasText(authentication.getName())
                ? authentication.getName()
                : SYSTEM;
    }
}