package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.CacheManager;

import java.util.Set;

public class CompanyWithActiveReceivingCacheWrapper extends CacheWrapper<Long, Set<Long>> {
    public CompanyWithActiveReceivingCacheWrapper(final CacheManager cacheManager) {
        super(cacheManager.getCache("companyWithActiveReceiving"));
    }
}
