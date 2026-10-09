package org.example.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.example.model.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ---- contract errors (ApiDefinition.yaml: application/problem+json ErrorResponse) ----

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
                                                                    HttpServletRequest request) {
        boolean hasMissingFields = ex.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> isMissingFieldError(error.getCode()));

        ErrorCode errorCode = hasMissingFields ? ErrorCode.MISSING_REQUIRED_FIELD : ErrorCode.INVALID_FIELD_FORMAT;
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse(errorCode, "Validation failed", request));
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        HttpStatus status = errorCode != null ? errorCode.getHttpStatus() : HttpStatus.BAD_REQUEST;

        ErrorResponse body = errorResponse(errorCode, status.getReasonPhrase(), request);
        for (var entry : ex.getAdditionalInformation().entrySet()) {
            body.putAdditionalInformationItem(entry.getKey(), entry.getValue());
        }

        return ResponseEntity.status(status).body(body);
    }

    private ErrorResponse errorResponse(ErrorCode errorCode, String title, HttpServletRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTitle(title);
        response.setStatus(errorCode != null ? errorCode.getHttpStatus().value() : HttpStatus.BAD_REQUEST.value());
        response.setCode(errorCode != null ? errorCode.getCode() : null);
        response.setTimestamp(OffsetDateTime.now(ZoneOffset.UTC));
        response.setInstance(request.getRequestURI());
        return response;
    }

    private boolean isMissingFieldError(String annotationCode) {
        if (annotationCode == null) {
            return false;
        }
        return annotationCode.equals("NotNull")
                || annotationCode.equals("NotBlank")
                || annotationCode.equals("NotEmpty");
    }

    // ---- legacy endpoints outside the generated contract (/energy/upload) ----

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParam(
            MissingServletRequestParameterException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                "Missing required parameter: " + ex.getParameterName(),
                request
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        return buildError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request
        );
    }

    private ResponseEntity<ApiError> buildError(
            HttpStatus status,
            String message,
            HttpServletRequest request
    ) {
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity.status(status).body(body);
    }
}
