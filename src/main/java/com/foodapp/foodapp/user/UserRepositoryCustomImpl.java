package com.foodapp.foodapp.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.foodapp.foodapp.administration.userAdministration.UsersSearchParams;
import com.foodapp.foodapp.common.UsersPagedResult;

import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UsersPagedResult searchOrders(UsersSearchParams params) {
        StringBuilder queryBuilder = new StringBuilder(UserSql.BASE_QUERY);
        StringBuilder countQueryBuilder = new StringBuilder(UserSql.BASE_COUNT_QUERY);
        Map<String, Object> valueParamsMap = new HashMap<>();

        if(params.getRole() != null) {
            queryBuilder.append(UserSql.ROLE_USER_QUERY);
            countQueryBuilder.append(UserSql.ROLE_USER_QUERY);
            valueParamsMap.put(UserSql.ROLE_PARAM, params.getRole());
        }

        queryBuilder.append(UserSql.DATE_FROM_QUERY);
        queryBuilder.append(UserSql.DATE_TO_QUERY);
        countQueryBuilder.append(UserSql.DATE_FROM_QUERY);
        countQueryBuilder.append(UserSql.DATE_TO_QUERY);
        valueParamsMap.put(UserSql.DATE_TO_PARAM, params.getDateTo() != null ? params.getDateTo().atTime(23, 59, 59) : null);
        valueParamsMap.put(UserSql.DATE_FROM_PARAM, params.getDateFrom() != null ? params.getDateFrom().atStartOfDay() : null);

        if(StringUtils.isNotEmpty(params.getGlobal())) {
            queryBuilder.append(UserSql.GLOBAL_QUERY);
            countQueryBuilder.append(UserSql.GLOBAL_QUERY);
            valueParamsMap.put(UserSql.GLOBAL_PARAM, params.getGlobal());
        }

        if(!Collections.isEmpty(params.getSorts())) {
            StringBuilder sortQueryBuilder = new StringBuilder();
            sortQueryBuilder.append(UserSql.BASE_SORT_QUERY);
            for(var sort : params.getSorts()) {
                sortQueryBuilder.append(" u.").append(sort.getField()).append(" ").append(sort.getDirection().name());
            }
            queryBuilder.append(sortQueryBuilder.toString());
        }

        Query userQuery = entityManager.createQuery(queryBuilder.toString(), User.class)
                                       .setFirstResult(params.getPage() * params.getSize())
                                       .setMaxResults(params.getSize());

        // Create the query for counting total results
        Query countQuery = entityManager.createQuery(countQueryBuilder.toString());

        valueParamsMap.forEach((param, value) -> {
            userQuery.setParameter(param, value);
            countQuery.setParameter(param, value);
        });

        // Fetch the orders
        List<User> users = userQuery.getResultList();

        // Fetch the total count
        long totalCount = (long) countQuery.getSingleResult();

        return UsersPagedResult.builder()
                               .users(UserMapper.toUsersDto(users))
                               .totalRecords(totalCount)
                               .build();
    }
}
