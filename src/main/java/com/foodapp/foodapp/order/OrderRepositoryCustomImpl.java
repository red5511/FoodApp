package com.foodapp.foodapp.order;

import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.SearchParams;
import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.foodapp.foodapp.order.OrderSql.*;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private static final Map<String, String> FIELDS_TO_CONVERTED_SQL =
            Map.of("name", " AND CAST(o.id AS string) LIKE :filter",
                    "price", " AND CAST(o.price AS string) LIKE :filter",
                    "status", " AND o.status IN (:statuses)");

    @Autowired
    private OrderMapper orderMapper;

    @PersistenceContext
    private EntityManager entityManager;

//    @Override
//    public OrdersPagedResult searchOrders(SearchParams searchParams) {
//        List<Order> results = new ArrayList<>();
//        long totalResults = 0;
//
//        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
//        Root<Order> orderRoot = criteriaQuery.from(Order.class);
//        List<Predicate> predicates = new ArrayList<>();
//
//        // Filter by statuses
//        if (searchParams.getStatuses() != null && !searchParams.getStatuses().isEmpty()) {
//            Predicate statusPredicate = orderRoot.get("status").in(searchParams.getStatuses());
//            predicates.add(statusPredicate);
//        }
//
//        // Filter by name (customer name)
//        if (searchParams.getName() != null && !searchParams.getName().isEmpty()) {
//            Predicate namePredicate = criteriaBuilder.like(orderRoot.get("customerName"), "%" + searchParams.getName() + "%");
//            predicates.add(namePredicate);
//        }
//
//        // Filter by price
//        if (searchParams.getPrice() != null && !searchParams.getPrice().isEmpty()) {
//            BigDecimal price = new BigDecimal(searchParams.getPrice());
//            Predicate pricePredicate = criteriaBuilder.equal(orderRoot.get("price"), price);
//            predicates.add(pricePredicate);
//        }
//
//        // Filter by description
//        if (searchParams.getDescription() != null && !searchParams.getDescription().isEmpty()) {
//            Predicate descriptionPredicate =
//                    criteriaBuilder.like(orderRoot.get("description"), "%" + searchParams.getDescription() + "%");
//            predicates.add(descriptionPredicate);
//        }
//
//        // Filter by date (if using dateParam)
//        if (searchParams.getDateParam() != null) {
//            LocalDate date = searchParams.getDateParam().getDate();
//            if (date != null) {
//                Predicate datePredicate = criteriaBuilder.equal(orderRoot.get("deliveryTime").as(LocalDate.class), date);
//                predicates.add(datePredicate);
//            }
//        }
//
//        // Filter by company ID
////        if (searchParams.getCompanyId() != null) {
////            Predicate companyPredicate = criteriaBuilder.equal(orderRoot.get("company").get("id"), searchParams.getCompanyId());
////            predicates.add(companyPredicate);
////        }
//
//        // Apply all predicates to the query
//        criteriaQuery.where(predicates.toArray(new Predicate[0]));
//
//        // Count total results for pagination
//        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
//        countQuery.select(criteriaBuilder.count(countQuery.from(Order.class))).where(predicates.toArray(new Predicate[0]));
//        totalResults = entityManager.createQuery(countQuery).getSingleResult();
//
//        // Sorting
////        if (searchParams.getSorts() != null && !searchParams.getSorts().isEmpty()) {
////            List<Order> orderList = new ArrayList<>();
////            for (Sort sort : searchParams.getSorts()) {
////                if ("asc".equalsIgnoreCase(sort.getDirection())) {
////                    orderList.add(criteriaBuilder.asc(orderRoot.get(sort.getProperty())));
////                } else {
////                    orderList.add(criteriaBuilder.desc(orderRoot.get(sort.getProperty())));
////                }
////            }
////            criteriaQuery.orderBy(orderList);
////        }
//
//        // Pagination
//        Query query = entityManager.createQuery(criteriaQuery);
//        query.setFirstResult(searchParams.getPage() * searchParams.getSize());
//        query.setMaxResults(searchParams.getSize());
//
//        results = query.getResultList();
//
//        return OrdersPagedResult.builder()
//                .orderList(OrderMapper.mapToOrderDtoList(results))
//                .totalRecords(totalResults)
//                .build();
//    }

    @Override
    public OrdersPagedResult searchOrders(SearchParams params) {
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);
        StringBuilder countQueryBuilder = new StringBuilder(BASE_COUNT_QUERY);

        Map<String, Object> valueParamsMap = new HashMap<>();

        if (params.getDateParam() != null && params.getDateParam().getDate() != null) {
            String dateQuery = null;
            if (DATE_IS_MODE.equals(params.getDateParam().getMode())) {
                dateQuery = DATE_IS_QUERY;
            } else if (DATE_IS_NOT_MODE.equals(params.getDateParam().getMode())) {
                dateQuery = DATE_IS_NOT_QUERY;
            } else if (DATE_BEFORE_MODE.equals(params.getDateParam().getMode())) {
                dateQuery = DATE_BEFORE_QUERY;
            } else if (DATE_AFTER_MODE.equals(params.getDateParam().getMode())) {
                dateQuery = DATE_AFTER_QUERY;
            }
            queryBuilder.append(dateQuery);
            countQueryBuilder.append(dateQuery);
            valueParamsMap.put(DELIVERY_DATE_PARAM, params.getDateParam().getDate());
        }
        if (StringUtils.isNotEmpty(params.getDescription())) {
            queryBuilder.append(DESCRIPTION_QUERY);
            countQueryBuilder.append(DESCRIPTION_QUERY);
            valueParamsMap.put(DESCRIPTION_PARAM, params.getDescription());
        }
        if (params.getPrice() != null) {
            queryBuilder.append(PRICE_QUERY);
            countQueryBuilder.append(PRICE_QUERY);
            valueParamsMap.put(PRICE_PARAM, params.getPrice());
        }
        if (!Collections.isEmpty(params.getStatuses())) {
            queryBuilder.append(STATUSES_QUERY);
            countQueryBuilder.append(STATUSES_QUERY);
            valueParamsMap.put(STATUSES_PARAM, params.getStatuses());
        }
        if (StringUtils.isNotEmpty(params.getGlobal())) {
            queryBuilder.append(GLOBAL_QUERY);
            countQueryBuilder.append(GLOBAL_QUERY);
            valueParamsMap.put(GLOBAL_PARAM, params.getGlobal());
        }

        if (!Collections.isEmpty(params.getSorts())) {
            StringBuilder sortQueryBuilder = new StringBuilder();
            sortQueryBuilder.append(BASE_SORT_QUERY);
            int i = 0;
            for (var sort : params.getSorts()) {
                sortQueryBuilder.append(" o.").append(sort.getField()).append(" ").append(sort.getDirection().name());
            }
            queryBuilder.append(sortQueryBuilder.toString());
        }

        Query orderQuery = entityManager.createQuery(queryBuilder.toString(), Order.class)
                .setParameter(COMPANY_ID_PARAM, params.getCompanyId())
                .setFirstResult(params.getPage() * params.getSize())
                .setMaxResults(params.getSize());

        // Create the query for counting total results
        Query countQuery = entityManager.createQuery(countQueryBuilder.toString())
                .setParameter(COMPANY_ID_PARAM, params.getCompanyId());

        valueParamsMap.forEach((param, value) -> {
            orderQuery.setParameter(param, value);
            countQuery.setParameter(param, value);
        });

        // Fetch the orders
        List<Order> orders = orderQuery.getResultList();

        // Fetch the total count
        long totalCount = (long) countQuery.getSingleResult();

        return OrdersPagedResult.builder()
                .orderList(orderMapper.mapToOrderDtoList(orders))
                .totalRecords(totalCount)
                .build();
    }

    private Object mapValues(String value) {
        return null;
    }


//    @Override
//    public List<Order> searchOrders(SearchParams params) {
//        StringBuilder query = new StringBuilder("SELECT o FROM Order o WHERE o.companyId = :companyId");
//
//        // Apply filters
//        if (params.getFilters() != null && !params.getFilters().isEmpty()) {
//            for (String filter : params.getFilters()) {
//                query.append(" AND o.name ILIKE '%' || :filter || '%'"); // Modify field as necessary
//            }
//        }
//
//        // Apply sorting
//        if (params.getSorts() != null && !params.getSorts().isEmpty()) {
//            query.append(" ORDER BY ");
//            for (Sort sort : params.getSorts()) {
//                query.append("o.").append(sort.getField()).append(" ").append(sort.getDirection()).append(", ");
//            }
//            // Remove the last comma
//            query.setLength(query.length() - 2);
//        }
//
//        // Add pagination
//        query.append(" LIMIT :size OFFSET :offset");
//
//        // Create the query using EntityManager
//        Query nativeQuery = entityManager.createQuery(query.toString(), Order.class)
//                .setParameter("companyId", params.getCompanyId())
//                .setParameter("size", params.getSize())
//                .setParameter("offset", params.getPage() * params.getSize());
//
//        // Apply filter parameters dynamically
//        if (params.getFilters() != null && !params.getFilters().isEmpty()) {
//            for (int i = 0; i < params.getFilters().size(); i++) {
//                nativeQuery.setParameter("filter" + (i + 1), params.getFilters().get(i));
//            }
//        }
//
//        return nativeQuery.getResultList();
//    }
}