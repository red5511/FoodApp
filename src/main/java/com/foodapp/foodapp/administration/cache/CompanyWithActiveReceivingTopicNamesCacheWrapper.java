package com.foodapp.foodapp.administration.cache;

import org.springframework.cache.CacheManager;

import java.util.Set;

public class CompanyWithActiveReceivingTopicNamesCacheWrapper extends CacheWrapper<Long, Set<String>> { // todo wydaje mi sie ze do wywalenia
    public CompanyWithActiveReceivingTopicNamesCacheWrapper(final CacheManager cacheManager) {
        super(cacheManager.getCache("companyWithActiveReceivingTopicNames"));
    }
}
