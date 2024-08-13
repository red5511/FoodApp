package com.foodapp.foodapp.advice;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BusinessException extends Throwable {

    private String errorCode;
    private HttpStatus status;
    private Map<String, Object> parameters;

    public BusinessException(final String errorCode) {
        this.errorCode = errorCode;
        this.status = HttpStatus.FORBIDDEN;
        this.parameters = new HashMap<>();
    }

    public BusinessException(final String errorCode,
                             final Map<String, String> parameters) {
        this.errorCode = errorCode;
        this.status = HttpStatus.FORBIDDEN;
        this.parameters = parameters != null ? new HashMap<>(parameters) : new HashMap<>();
    }

    public BusinessException(final String errorCode,
                             final Map<String, String> parameters,
                             final HttpStatus httpStatus) {
        this.errorCode = errorCode;
        this.status = httpStatus;
        this.parameters = parameters != null ? new HashMap<>(parameters) : new HashMap<>();
    }
}
