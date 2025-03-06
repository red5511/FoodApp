package com.foodapp.foodapp.config;

import com.foodapp.foodapp.forDevelopment.TechnicalContextAspect;
import com.foodapp.foodapp.forDevelopment.TechnicalContextProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TechConfiguration {
    @Bean
    public TechnicalContextProvider technicalContextProvider() {
        return new TechnicalContextProvider();
    }

    @Bean
    public TechnicalContextAspect technicalContextAspect(final TechnicalContextProvider technicalContextProvider){
        return new TechnicalContextAspect(technicalContextProvider);
    }
}
