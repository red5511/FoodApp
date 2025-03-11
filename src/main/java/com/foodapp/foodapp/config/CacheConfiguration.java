package com.foodapp.foodapp.config;


import com.foodapp.foodapp.administration.cache.CompanyWithActiveReceivingUsersCacheWrapper;
import com.foodapp.foodapp.administration.cache.LoginAttemptCacheWrapper;
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
    public UsersConnectedToWebSocketCacheWrapper usersConnectedToWebCacheWrapper() {
        return new UsersConnectedToWebSocketCacheWrapper(buildCacheManager(5, TimeUnit.MINUTES));
    }

    @Bean
    public CompanyWithActiveReceivingUsersCacheWrapper companyWithActiveReceivingUsersCacheWrapper() {
        return new CompanyWithActiveReceivingUsersCacheWrapper(buildCacheManager(5, TimeUnit.MINUTES));
    }

    @Bean
    public LoginAttemptCacheWrapper loginAttemptCacheWrapper(){
        return new LoginAttemptCacheWrapper(buildCacheManager(10, TimeUnit.MINUTES));
    }

//    @Bean
//    public CompanyWithActiveReceivingTopicNamesCacheWrapper companyWithActiveReceivingTopicNamesCacheWrapper(
//            final CacheManager cacheManager) {
//        return new CompanyWithActiveReceivingTopicNamesCacheWrapper(cacheManager);
//    }


    public CaffeineCacheManager buildCacheManager(final int duration, final TimeUnit timeUnit) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .expireAfterAccess(duration, timeUnit) // Set eviction time to 5 minutes
        );
        return cacheManager;
    }
}
