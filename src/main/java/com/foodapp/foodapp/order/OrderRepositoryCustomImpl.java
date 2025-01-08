package com.foodapp.foodapp.order;

import static com.foodapp.foodapp.order.OrderSql.BASE_COUNT_QUERY;
import static com.foodapp.foodapp.order.OrderSql.BASE_QUERY;
import static com.foodapp.foodapp.order.OrderSql.BASE_SORT_QUERY;
import static com.foodapp.foodapp.order.OrderSql.COMPANY_IDS_PARAM;
import static com.foodapp.foodapp.order.OrderSql.DATE_FROM_PARAM;
import static com.foodapp.foodapp.order.OrderSql.DATE_IS_BETWEEN_QUERY;
import static com.foodapp.foodapp.order.OrderSql.DATE_TO_PARAM;
import static com.foodapp.foodapp.order.OrderSql.GLOBAL_PARAM;
import static com.foodapp.foodapp.order.OrderSql.GLOBAL_QUERY;
import static com.foodapp.foodapp.order.OrderSql.PRICE_PARAM;
import static com.foodapp.foodapp.order.OrderSql.PRICE_QUERY;
import static com.foodapp.foodapp.order.OrderSql.STATUSES_PARAM;
import static com.foodapp.foodapp.order.OrderSql.STATUSES_QUERY;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.SearchParams;

import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrdersPagedResult searchOrders(SearchParams params) {
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        StringBuilder countQueryBuilder = new StringBuilder(BASE_COUNT_QUERY);

        Map<String, Object> valueParamsMap = new HashMap<>();

        queryBuilder.append(DATE_IS_BETWEEN_QUERY);
        countQueryBuilder.append(DATE_IS_BETWEEN_QUERY);
        valueParamsMap.put(DATE_TO_PARAM, params.getDateTo().atTime(23, 59));
        valueParamsMap.put(DATE_FROM_PARAM, params.getDateFrom().atStartOfDay());

        if(params.getPrice() != null) {
            queryBuilder.append(PRICE_QUERY);
            countQueryBuilder.append(PRICE_QUERY);
            valueParamsMap.put(PRICE_PARAM, params.getPrice());
        }
        if(!Collections.isEmpty(params.getStatuses())) {
            queryBuilder.append(STATUSES_QUERY);
            countQueryBuilder.append(STATUSES_QUERY);
            valueParamsMap.put(STATUSES_PARAM, params.getStatuses());
        }
        if(StringUtils.isNotEmpty(params.getGlobal())) {
            queryBuilder.append(GLOBAL_QUERY);
            countQueryBuilder.append(GLOBAL_QUERY);
            valueParamsMap.put(GLOBAL_PARAM, params.getGlobal());
        }

        if(!Collections.isEmpty(params.getSorts())) {
            StringBuilder sortQueryBuilder = new StringBuilder();
            sortQueryBuilder.append(BASE_SORT_QUERY);
            for(var sort : params.getSorts()) {
                sortQueryBuilder.append(" o.").append(sort.getField()).append(" ").append(sort.getDirection().name());
            }
            queryBuilder.append(sortQueryBuilder.toString());
        }

        Query orderQuery = entityManager.createQuery(queryBuilder.toString(), Order.class)
                                        .setParameter(COMPANY_IDS_PARAM, params.getCompanyIds())
                                        .setFirstResult(params.getPage() * params.getSize())
                                        .setMaxResults(params.getSize());

        // Create the query for counting total results
        Query countQuery = entityManager.createQuery(countQueryBuilder.toString())
                                        .setParameter(COMPANY_IDS_PARAM, params.getCompanyIds());

        valueParamsMap.forEach((param, value) -> {
            orderQuery.setParameter(param, value);
            countQuery.setParameter(param, value);
        });

        // Fetch the orders
        List<Order> orders = orderQuery.getResultList();

        // Fetch the total count
        long totalCount = (long) countQuery.getSingleResult();

        return OrdersPagedResult.builder()
                                .orderList(OrderMapper.mapToOrderDtoList(orders))
                                .totalRecords(totalCount)
                                .build();
    }
}
