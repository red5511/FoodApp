package com.foodapp.foodapp.config;

import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@AllArgsConstructor
public class ApplicationAuditAware implements AuditorAware<Long> {
    private final ContextProvider contextProvider;

    @Override
    public Optional<Long> getCurrentAuditor() {
        return contextProvider.getUserId();
    }

}