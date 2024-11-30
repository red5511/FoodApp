package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class CacheWrapper<CACHED_CLASS> {
    private final Cache cache;

    public CacheWrapper(Cache cache) {
        this.cache = cache;
    }

    public Set<Long> getAllIds() {
        Set<Long> ids = new HashSet<>();

        if (cache != null) {
            // Attempt to cast the native cache to a Map
            CaffeineCache caffeineCache = (CaffeineCache) cache;

            //Cache<Long, CACHED_CLASS> caffeineCache = (Cache<Long, CACHED_CLASS>) nativeCache;

            // Access the cache as a Map
            var mapCache = caffeineCache.getNativeCache().asMap();
            mapCache.forEach((k, v) -> {
                Long key = (Long) k;
                CACHED_CLASS value = (CACHED_CLASS) v;
                if (key != null) {
                    ids.add(key);
                }
            });
        }
        return ids;
    }

    public Map<Long, CACHED_CLASS> getEntries() {
        Map<Long, CACHED_CLASS> entries = new HashMap<>();

        if (cache != null) {
            // Attempt to cast the native cache to a Map
            CaffeineCache caffeineCache = (CaffeineCache) cache;

            //Cache<Long, CACHED_CLASS> caffeineCache = (Cache<Long, CACHED_CLASS>) nativeCache;

            // Access the cache as a Map
            var mapCache = caffeineCache.getNativeCache().asMap();
            mapCache.forEach((k, v) -> {
                Long key = (Long) k;
                CACHED_CLASS value = (CACHED_CLASS) v;
                if (key != null && value != null) {
                    entries.put(key, value);
                }
            });
        }
        return entries;
    }

    public void put(Long id, CACHED_CLASS entry) {
        cache.put(id, entry);
    }
}
