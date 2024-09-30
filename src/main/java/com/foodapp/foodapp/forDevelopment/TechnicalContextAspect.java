package com.foodapp.foodapp.forDevelopment;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TechnicalContextAspect {
    private final TechnicalContextProvider technicalContextProvider;

    public TechnicalContextAspect(TechnicalContextProvider technicalContextProvider) {
        this.technicalContextProvider = technicalContextProvider;
    }

    @Before("@annotation(TechnicalContextDev)")
    public void applyTechnicalContext() {
        technicalContextProvider.createTechnicalContext();
    }
}
