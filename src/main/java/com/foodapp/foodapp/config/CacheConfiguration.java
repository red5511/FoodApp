package com.foodapp.foodapp.config;


import com.foodapp.foodapp.administration.cache.UsersConnectedToWebCacheWrapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {
    @Bean
    public UsersConnectedToWebCacheWrapper usersConnectedToWebCacheWrapper(final CacheManager cacheManager) {
        return new UsersConnectedToWebCacheWrapper(cacheManager);
    }

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.MINUTES) // Set eviction time to 5 minutes
        );
        return cacheManager;
    }
}
