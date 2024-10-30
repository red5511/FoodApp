package com.foodapp.foodapp.order;

import com.foodapp.foodapp.common.CommonUtils;
import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.SearchParams;
import com.foodapp.foodapp.common.Sort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    private static final Map<String, String> FIELDS_TO_CONVERTED_SQL =
            Map.of("name", " AND CAST(o.id AS string) LIKE :filter",
                    "price", " AND CAST(o.price AS string) LIKE :filter",
                    "status", " AND o.status IN (:statuses)");

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrdersPagedResult searchOrders(SearchParams searchParams) {
        List<Order> results = new ArrayList<>();
        long totalResults = 0;

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteriaQuery.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();

        // Filter by statuses
        if (searchParams.getStatuses() != null && !searchParams.getStatuses().isEmpty()) {
            Predicate statusPredicate = orderRoot.get("status").in(searchParams.getStatuses());
            predicates.add(statusPredicate);
        }

        // Filter by name (customer name)
        if (searchParams.getName() != null && !searchParams.getName().isEmpty()) {
            Predicate namePredicate = criteriaBuilder.like(orderRoot.get("customerName"), "%" + searchParams.getName() + "%");
            predicates.add(namePredicate);
        }

        // Filter by price
        if (searchParams.getPrice() != null && !searchParams.getPrice().isEmpty()) {
            BigDecimal price = new BigDecimal(searchParams.getPrice());
            Predicate pricePredicate = criteriaBuilder.equal(orderRoot.get("price"), price);
            predicates.add(pricePredicate);
        }

        // Filter by description
        if (searchParams.getDescription() != null && !searchParams.getDescription().isEmpty()) {
            Predicate descriptionPredicate = criteriaBuilder.like(orderRoot.get("description"), "%" + searchParams.getDescription() + "%");
            predicates.add(descriptionPredicate);
        }

        // Filter by date (if using dateParam)
        if (searchParams.getDateParam() != null) {
            LocalDate date = searchParams.getDateParam().getDate();
            if (date != null) {
                Predicate datePredicate = criteriaBuilder.equal(orderRoot.get("deliveryTime").as(LocalDate.class), date);
                predicates.add(datePredicate);
            }
        }

        // Filter by company ID
//        if (searchParams.getCompanyId() != null) {
//            Predicate companyPredicate = criteriaBuilder.equal(orderRoot.get("company").get("id"), searchParams.getCompanyId());
//            predicates.add(companyPredicate);
//        }

        // Apply all predicates to the query
        criteriaQuery.where(predicates.toArray(new Predicate[0]));

        // Count total results for pagination
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        countQuery.select(criteriaBuilder.count(countQuery.from(Order.class))).where(predicates.toArray(new Predicate[0]));
        totalResults = entityManager.createQuery(countQuery).getSingleResult();

        // Sorting
//        if (searchParams.getSorts() != null && !searchParams.getSorts().isEmpty()) {
//            List<Order> orderList = new ArrayList<>();
//            for (Sort sort : searchParams.getSorts()) {
//                if ("asc".equalsIgnoreCase(sort.getDirection())) {
//                    orderList.add(criteriaBuilder.asc(orderRoot.get(sort.getProperty())));
//                } else {
//                    orderList.add(criteriaBuilder.desc(orderRoot.get(sort.getProperty())));
//                }
//            }
//            criteriaQuery.orderBy(orderList);
//        }

        // Pagination
        Query query = entityManager.createQuery(criteriaQuery);
        query.setFirstResult(searchParams.getPage() * searchParams.getSize());
        query.setMaxResults(searchParams.getSize());

        results = query.getResultList();

        return OrdersPagedResult.builder()
                .orderList(OrderMapper.mapToOrderDtoList(results))
                .totalRecords(totalResults)
                .build();
    }

//    @Override
//    public OrdersPagedResult searchOrders(SearchParams params) {
//        // Query to get the filtered and sorted orders (paged results)
//        StringBuilder query = new StringBuilder("SELECT o FROM Order o WHERE o.company.id = :companyId");
//
//        // Query to get the total count
//        StringBuilder countQuery = new StringBuilder("SELECT COUNT(o) FROM Order o WHERE o.company.id = :companyId");


//        @Query("""
//    SELECT o FROM Order o
//    WHERE o.company.id = :companyId
//      AND (:statuses IS NULL OR o.status IN :statuses)
//    """)
//        List<Order> findByCompanyIdAndStatuses(
//                @Param("companyId") Long companyId,
//                @Param("statuses") List<OrderStatus> statuses
//);

        // Apply filters
//        if (params.getFilters() != null && !params.getFilters().isEmpty()) {
//            for (int i = 0; i < params.getFilters().size(); i++) {
//                var filter = params.getFilters().get(i);
//                String fieldName = filter.getFieldName();
//
//                if (filter.getValues().size() == 1){
//                    if (FIELDS_TO_CONVERTED_SQL.containsKey(fieldName)) {
//                        filter.getValues().set(0, CommonUtils.extractNumbers(filter.getValues().get(0)));
//                        query.append(FIELDS_TO_CONVERTED_SQL.get(fieldName)).append(i + 1);
//                        countQuery.append(FIELDS_TO_CONVERTED_SQL.get(fieldName)).append(i + 1);
//                    } else {
//                        // Append the actual field name directly
//                        query.append(" AND o.").append(fieldName).append(" ILIKE :filter").append(i + 1);
//                        countQuery.append(" AND o.").append(fieldName).append(" ILIKE :filter").append(i + 1);
//                    }
//                }
//                else{
//                    query.append(" AND o.").append(fieldName).append(" IN (:filter").append(i + 1).append(")");
//                    countQuery.append(" AND o.").append(fieldName).append(" IN (:filter").append(i + 1).append(")");
//                }
//            }
//        }
//
//        // Apply sorting
//        if (params.getSorts() != null && !params.getSorts().isEmpty()) {
//            query.append(" ORDER BY ");
//            for (Sort sort : params.getSorts()) {
//                query.append("o.").append(sort.getField()).append(" ").append(sort.getDirection()).append(", ");
//            }
//            query.setLength(query.length() - 2); // Remove the last comma and space
//        }
//
//        // Create the query for fetching orders
//        Query orderQuery = entityManager.createQuery(query.toString(), Order.class)
//                .setParameter("companyId", params.getCompanyId())
//                .setFirstResult(params.getPage() * params.getSize())
//                .setMaxResults(params.getSize());
//
//        // Create the query for counting total results
//        Query countQueryObject = entityManager.createQuery(countQuery.toString())
//                .setParameter("companyId", params.getCompanyId());
//
//        // Apply filter parameters dynamically for both queries
//        if (params.getFilters() != null && !params.getFilters().isEmpty()) {
//            for (int i = 0; i < params.getFilters().size(); i++) {
//                var filter = params.getFilters().get(i);
//                if (filter.getValues().size() == 1){
//                    String filterParam = "%" + filter.getValues().get(0) + "%";
//                    orderQuery.setParameter("filter" + (i + 1), filterParam);
//                    countQueryObject.setParameter("filter" + (i + 1), filterParam);
//                }
//                else{
//                    String result = String.join(",", filter.getValues());
//                    String filterParam = "%" + result + "%";
//                    orderQuery.setParameter("filter" + (i + 1), filterParam);
//                    countQueryObject.setParameter("filter" + (i + 1), filterParam);
//                }
//            }
//        }
//
//        // Fetch the orders
//        List<Order> orders = orderQuery.getResultList();
//
//        // Fetch the total count
//        long totalCount = (long) countQueryObject.getSingleResult();
//
        // Return both the results and total count
//        return OrdersPagedResult.builder()
//                .orderList(OrderMapper.mapToOrderDtoList(List.of()))
//                .totalRecords(0)
//                .build();
//    }

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