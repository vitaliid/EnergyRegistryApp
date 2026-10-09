package org.example.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> additionalInformation;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.additionalInformation = new HashMap<>();
    }

    public BusinessException(ErrorCode errorCode, Map<String, Object> additionalInformation) {
        super(errorCode.getCode());
        this.errorCode = errorCode;
        this.additionalInformation = additionalInformation;
    }
}
