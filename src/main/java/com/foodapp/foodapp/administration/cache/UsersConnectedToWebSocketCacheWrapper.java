package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.CacheManager;

import java.util.Set;

public class UsersConnectedToWebSocketCacheWrapper extends CacheWrapper<Long, Set<Long>> {
    public UsersConnectedToWebSocketCacheWrapper(final CacheManager cacheManager) {
        super(cacheManager.getCache("usersConnectedToWebSocket"));
    }
}
