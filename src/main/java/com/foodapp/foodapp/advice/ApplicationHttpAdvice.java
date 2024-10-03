package com.foodapp.foodapp.advice;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity handleBusinessException(final DataIntegrityViolationException ex) {
        return handleException();
    }

    @ExceptionHandler({JwtException.class})
    public ResponseEntity handleBusinessException(final JwtException ex) {
        var businessException = new BusinessException(ex.getMessage());
        return handleException(businessException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity handleBusinessException(final AuthenticationException ex) {
        if (ex instanceof BadCredentialsException) {
            return handleException(ex, HttpStatus.FORBIDDEN);
        } else if (ex instanceof UsernameNotFoundException) {
            return handleException(ex, HttpStatus.UNAUTHORIZED);
        }
        return handleException();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(final MethodArgumentNotValidException ex) {
        var firstEx = ex.getBindingResult().getAllErrors().stream().findFirst().get();
        var businessException = new BusinessException(firstEx.getDefaultMessage());
        return handleException(businessException, businessException.getStatus());
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity handleExpiredJwtException(final SignatureException ex) {
        var businessException = new BusinessException(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
        return handleException(businessException, businessException.getStatus());
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity handleSecurityException(final Exception ex) {
        var businessException = new BusinessException(ex.getMessage());
        return handleException(businessException, businessException.getStatus());
    }


//    @ExceptionHandler(Exception.class)
//    public ResponseEntity handleDefaultException(final Exception ex) {
//        var businessException = new BusinessException(ex.getMessage(), null, HttpStatus.EXPECTATION_FAILED);
//        return handleException(businessException, businessException.getStatus());
//    }

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
