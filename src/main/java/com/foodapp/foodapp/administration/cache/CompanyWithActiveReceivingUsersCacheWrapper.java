package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.CacheManager;

import java.util.Set;

public class CompanyWithActiveReceivingUsersCacheWrapper extends CacheWrapper<Long, Set<Long>> {
    public CompanyWithActiveReceivingUsersCacheWrapper(final CacheManager cacheManager) {
        super(cacheManager.getCache("companyWithActiveReceivingUsers"));
    }
}
