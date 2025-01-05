package com.foodapp.foodapp.administration.cache;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Slf4j
@Getter
public class CacheService {
    private final CompanyWithActiveReceivingUsersCacheWrapper companyUsersCache;
    private final UsersConnectedToWebSocketCacheWrapper usersConnectedToWebSocketCache;

    public void prepareCacheForReceivingOrders(final Long userId,
                                               final Set<Long> companyIdsToAdd,
                                               final Set<Long> companyIdsToRemove) {
        addToUserCache(userId, companyIdsToAdd);
        removeFromUserCache(userId, companyIdsToRemove);
        companyIdsToAdd.forEach(companyId -> addToCompanyCache(userId, companyId));
        companyIdsToRemove.forEach(companyId -> removeCompanyCache(userId, companyId));
    }

    private void addToCompanyCache(final Long userId, final Long companyId) {
        var cachedCompanyUsersSet = companyUsersCache.get(companyId);
        if (!CollectionUtils.isEmpty(cachedCompanyUsersSet)) {
            if (cachedCompanyUsersSet.contains(userId)) {
                log.info("User id already in cache (this might be good)");
            } else {
                cachedCompanyUsersSet.add(userId);
            }
        } else {
            companyUsersCache.put(companyId, new HashSet<>(Set.of(userId)));
        }
    }

    private void addToUserCache(final Long userId, final Set<Long> companyIds) {
        var companies = usersConnectedToWebSocketCache.get(userId);
        if (!CollectionUtils.isEmpty(companies)) {
            companies.addAll(companyIds);
        } else {
            usersConnectedToWebSocketCache.put(userId, companyIds);
        }
    }

    private void removeFromUserCache(final Long userId, final Set<Long> companyIdsToRemove) {
        var companiesIds = usersConnectedToWebSocketCache.get(userId);
        if (!CollectionUtils.isEmpty(companiesIds) && !CollectionUtils.isEmpty(companyIdsToRemove)) {
            System.out.println("removeFromUserCache");
            log.info(companiesIds.toString());
            log.info(companyIdsToRemove.toString());
            companiesIds.removeAll(companyIdsToRemove);
            if (companiesIds.isEmpty()) {
                usersConnectedToWebSocketCache.remove(userId);
            }
        }
    }

    public boolean validateIfCompanyIsReceivingOrders(final Long companyId) {
        System.out.println("validateIfCompanyIsReceivingOrders");
        System.out.println("companyCache " + companyUsersCache.getEntries());
        System.out.println("usersCache " + usersConnectedToWebSocketCache.getEntries());
        return companyUsersCache.getAllIds().contains(companyId);
    }

    public void removeCacheFroReceivingOrders(final Long userId, final Set<Long> companyIds) {
        usersConnectedToWebSocketCache.remove(userId);
        companyIds.forEach(companyId -> removeCompanyCache(userId, companyId));
    }

    private void removeCompanyCache(final Long userId, final Long companyId) {
        var cachedCompanyUsersSet = companyUsersCache.get(companyId);
        if (!CollectionUtils.isEmpty(cachedCompanyUsersSet)) {
            if (cachedCompanyUsersSet.size() == 1) {
                if (cachedCompanyUsersSet.contains(userId)) {
                    companyUsersCache.remove(companyId);
                } else {
                    log.error("Company cache didnt contain provided userId");
                }
            } else {
                cachedCompanyUsersSet.remove(userId);
            }
        } else {
            log.error("Company cache was empty couldnt remove provided userId");
        }
    }

}