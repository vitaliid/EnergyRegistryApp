package org.example.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Machine-readable application error codes as they appear in {@code ErrorResponse.code}.
 * The string values are part of the API contract (see the examples in ApiDefinition.yaml).
 */
@Getter
public enum ErrorCode {
    MISSING_REQUIRED_FIELD("REQUIRED_FIELD_MISSING", HttpStatus.BAD_REQUEST),
    INVALID_FIELD_FORMAT("INVALID_FIELD_FORMAT", HttpStatus.BAD_REQUEST),
    DUPLICATE_NAME_IN_PARENT("DUPLICATE_NAME_IN_PARENT", HttpStatus.BAD_REQUEST),
    INVALID_PARENT("INVALID_PARENT", HttpStatus.BAD_REQUEST),
    ENTITY_NOT_FOUND("ENTITY_NOT_FOUND", HttpStatus.NOT_FOUND),
    INTERNAL_DATA_ERROR("INTERNAL_DATA_ERROR", HttpStatus.INTERNAL_SERVER_ERROR),

    CONSUMPTION_NOT_POSITIVE("CONSUMPTION_NOT_POSITIVE", HttpStatus.BAD_REQUEST),
    END_BEFORE_START("END_DATE_BEFORE_START_DATE", HttpStatus.BAD_REQUEST),
    CONSUMPTION_TYPE_CHANGE_NOT_ALLOWED("CONSUMPTION_TYPE_CHANGE_NOT_ALLOWED", HttpStatus.BAD_REQUEST),

    INVALID_ASSIGNMENT("INVALID_ASSIGNMENT", HttpStatus.BAD_REQUEST),
    ACCESS_DENIED("ACCESS_DENIED", HttpStatus.FORBIDDEN),
    EMAIL_ALREADY_EXISTS("EMAIL_ALREADY_EXISTS", HttpStatus.CONFLICT),
    SELF_REASSIGNMENT_NOT_ALLOWED("SELF_REASSIGNMENT_NOT_ALLOWED", HttpStatus.FORBIDDEN),
    SELF_DEACTIVATION_NOT_ALLOWED("SELF_DEACTIVATION_NOT_ALLOWED", HttpStatus.FORBIDDEN),
    SELF_ROLE_CHANGE_NOT_ALLOWED("SELF_ROLE_CHANGE_NOT_ALLOWED", HttpStatus.FORBIDDEN),
    LAST_ADMIN_CONFIRMATION_REQUIRED("LAST_ADMIN_CONFIRMATION_REQUIRED", HttpStatus.CONFLICT),
    KEYCLOAK_UNAVAILABLE("USER_MANAGEMENT_UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE);

    private final String code;
    private final HttpStatus httpStatus;

    ErrorCode(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
