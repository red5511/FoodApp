package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.CacheManager;

public class UsersConnectedToWebSocketCacheWrapper extends CacheWrapper<Long, UsersConnectedToWebSocketEntry> {
    public UsersConnectedToWebSocketCacheWrapper(final CacheManager cacheManager) {
        super(cacheManager.getCache("usersConnectedToWebSocket"));
    }
}
