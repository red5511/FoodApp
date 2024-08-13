package com.foodapp.foodapp.advice;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@AllArgsConstructor
public class ApplicationHttpAdvice {
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity handleBusinessException(final BusinessException ex) {
        return handleException(ex, ex.getStatus());
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity handleBusinessException(final AuthenticationException ex) {
        if (ex instanceof BadCredentialsException) {
            return handleException(ex, HttpStatus.FORBIDDEN);
        }
        return handleException();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        var firstEx = ex.getBindingResult().getAllErrors().stream().findFirst().get();
        var businessException = new BusinessException(firstEx.getDefaultMessage());
        return handleException(businessException, businessException.getStatus());
    }

    private ResponseEntity<BusinessExceptionResponse> handleException(final BusinessException ex, final HttpStatus httpStatus) {
        var errorResponse = BusinessExceptionResponse.builder()
                .errorCode(ex.getErrorCode())
                .parameters(ex.getParameters())
                .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private ResponseEntity<BusinessExceptionResponse> handleException(final AuthenticationException ex,
                                                                      final HttpStatus httpStatus) {
        var errorResponse = BusinessExceptionResponse.builder()
                .errorCode(ex.getMessage())
                .build();
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    private ResponseEntity<BusinessExceptionResponse> handleException() {
        var errorResponse = BusinessExceptionResponse.builder()
                .errorCode("Default")
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }
}
