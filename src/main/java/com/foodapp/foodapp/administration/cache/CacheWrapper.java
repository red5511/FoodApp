package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class CacheWrapper<KEY, CACHED_VALUE> {
    private final Cache cache;

    public CacheWrapper(Cache cache) {
        this.cache = cache;
    }

    public Set<Long> getAllIds() {
        Set<Long> ids = new HashSet<>();

        if (cache != null) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            var mapCache = caffeineCache.getNativeCache().asMap();
            mapCache.forEach((k, v) -> {
                Long key = (Long) k;
                CACHED_VALUE value = (CACHED_VALUE) v;
                if (key != null) {
                    ids.add(key);
                }
            });
        }
        return ids;
    }

    public Map<Long, CACHED_VALUE> getEntries() {
        Map<Long, CACHED_VALUE> entries = new HashMap<>();

        if (cache != null) {
            CaffeineCache caffeineCache = (CaffeineCache) cache;
            var mapCache = caffeineCache.getNativeCache().asMap();
            mapCache.forEach((k, v) -> {
                Long key = (Long) k;
                CACHED_VALUE value = (CACHED_VALUE) v;
                if (key != null && value != null) {
                    entries.put(key, value);
                }
            });
        }
        return entries;
    }

    public void put(KEY key, CACHED_VALUE value) {
        cache.put(key, value);
    }

    public CACHED_VALUE get(KEY key) {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        var mapCache = caffeineCache.getNativeCache().asMap();
        return (CACHED_VALUE) mapCache.get(key);
    }

    public void remove(KEY key) {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        cache.evictIfPresent(key);
    }

    public CACHED_VALUE getOrDefault(KEY key, CACHED_VALUE defaultValue) {
        CaffeineCache caffeineCache = (CaffeineCache) cache;
        var mapCache = caffeineCache.getNativeCache().asMap();
        var result = (CACHED_VALUE) mapCache.get(key);
        return result == null ? defaultValue : result;
    }}
