package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.CacheManager;

public class LoginAttemptCacheWrapper extends CacheWrapper<String, Integer> {
    public LoginAttemptCacheWrapper(final CacheManager cacheManager) {
        super(cacheManager.getCache("loginAttempt"));
    }
}
