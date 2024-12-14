package com.foodapp.foodapp.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("Request URL: {} | Method: {}", request.getRequestURI(), request.getMethod());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex != null) {
            log.error("Exception " + ex);
            log.error(Arrays.toString(ex.getStackTrace()));
            response.setStatus(403);
        }
        log.info("Response Status: {}", response.getStatus());
    }
}
