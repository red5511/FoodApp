package com.foodapp.foodapp.administration.cache;

import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
public class CacheService {
    private final CompanyWithActiveReceivingCacheWrapper companyWithActiveReceivingCacheWrapper;
    private final UsersConnectedToWebSocketCacheWrapper usersConnectedToWebSocketCacheWrapper;

    public void prepareCacheForReceivingOrders(final Long userId, final Long companyId) {
        var cachedSet = companyWithActiveReceivingCacheWrapper.get(companyId);
        if (!CollectionUtils.isEmpty(cachedSet)) {
            cachedSet.add(userId);
        } else {
            companyWithActiveReceivingCacheWrapper.put(companyId, new HashSet<>(Set.of(userId)));
        }
        usersConnectedToWebSocketCacheWrapper.put(userId, UsersConnectedToWebSocketEntry.builder()
                .companyId(companyId)
                .userId(userId)
                .build()
        );
    }


    public boolean validateIfCompanyIsReceivingOrders(final Long companyId) {
        System.out.println("cacheService"+companyWithActiveReceivingCacheWrapper.getAllIds());
        return companyWithActiveReceivingCacheWrapper.get(companyId) != null;
    }

    public void removeCacheFroReceivingOrders(final Long userId, final Long companyId) {
        usersConnectedToWebSocketCacheWrapper.remove(userId);
        companyWithActiveReceivingCacheWrapper.remove(companyId);

        System.out.println("cacheServiceV2"+companyWithActiveReceivingCacheWrapper.getAllIds());

    }
}
