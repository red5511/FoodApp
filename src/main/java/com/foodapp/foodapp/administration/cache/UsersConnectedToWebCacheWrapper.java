package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.CacheManager;

public class UsersConnectedToWebCacheWrapper extends CacheWrapper<UsersConnectedToWebSocketEntry> {
    public UsersConnectedToWebCacheWrapper(CacheManager cacheManager) {
        super(cacheManager.getCache("usersConnectedToWebSocket"));
    }
}
