package org.example.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

/**
 * Reads the Keycloak user id ({@code sub}) of the authenticated subject from the security context.
 * Works for JWT resource servers ({@link Jwt} principal) as well as opaque-token introspection
 * ({@link OAuth2AuthenticatedPrincipal}).
 */
@Slf4j
public final class AuthenticatedSubject {

    private AuthenticatedSubject() {
    }

    public static Optional<UUID> currentId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return Optional.empty();
        }

        String subject = switch (authentication.getPrincipal()) {
            case Jwt jwt -> jwt.getSubject();
            case OAuth2AuthenticatedPrincipal principal ->
                    principal.getAttribute(OAuth2TokenIntrospectionClaimNames.SUB);
            default -> {
                log.warn("Principal of type {} carries no subject claim",
                        authentication.getPrincipal().getClass().getName());
                yield null;
            }
        };

        if (subject == null) {
            log.warn("Token carries no 'sub' claim");
            return Optional.empty();
        }

        try {
            return Optional.of(UUID.fromString(subject));
        } catch (IllegalArgumentException ex) {
            log.warn("Token subject '{}' is not a UUID and cannot identify a user row", subject);
            return Optional.empty();
        }
    }
}
