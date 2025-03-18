package com.foodapp.foodapp.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientIp = getClientIp(request);
        log.info("URL: {} | Method: {} | IP: {}",
                request.getRequestURI(),
                request.getMethod(),
                clientIp);
        return true; // Allow request to proceed
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        if (ex != null) {
            log.error("Exception occurred: ", ex);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
        }
        log.info("Response Status: {}", response.getStatus());
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For"); // Get IP from proxy headers
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr(); // Fallback to standard method
        }
        return ip;
    }
}

