package com.foodapp.foodapp.config;


import com.foodapp.foodapp.administration.cache.CompanyWithActiveReceivingTopicNamesCacheWrapper;
import com.foodapp.foodapp.administration.cache.CompanyWithActiveReceivingUsersCacheWrapper;
import com.foodapp.foodapp.administration.cache.UsersConnectedToWebSocketCacheWrapper;
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
    public UsersConnectedToWebSocketCacheWrapper usersConnectedToWebCacheWrapper(final CacheManager cacheManager) {
        return new UsersConnectedToWebSocketCacheWrapper(cacheManager);
    }

    @Bean
    public CompanyWithActiveReceivingUsersCacheWrapper companyWithActiveReceivingUsersCacheWrapper(final CacheManager cacheManager) {
        return new CompanyWithActiveReceivingUsersCacheWrapper(cacheManager);
    }

//    @Bean
//    public CompanyWithActiveReceivingTopicNamesCacheWrapper companyWithActiveReceivingTopicNamesCacheWrapper(
//            final CacheManager cacheManager) {
//        return new CompanyWithActiveReceivingTopicNamesCacheWrapper(cacheManager);
//    }

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES) // Set eviction time to 5 minutes
        );
        return cacheManager;
    }
}
