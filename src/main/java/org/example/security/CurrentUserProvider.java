package org.example.security;

import org.example.exception.BusinessException;
import org.example.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Resolves the authenticated principal to the primary key of {@code app_user}, or rejects the
 * request. See {@link AuthenticatedSubject} for where the id comes from.
 */
@Component
public class CurrentUserProvider {

    public UUID requireCurrentUserId() {
        return AuthenticatedSubject.currentId()
                .orElseThrow(() -> new BusinessException(ErrorCode.ACCESS_DENIED));
    }
}
