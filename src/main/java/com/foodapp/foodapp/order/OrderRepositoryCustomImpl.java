package com.foodapp.foodapp.order;

import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.SearchParams;
import com.foodapp.foodapp.common.Sort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public OrdersPagedResult searchOrders(SearchParams params) {
        // Query to get the filtered and sorted orders (paged results)
        StringBuilder query = new StringBuilder("SELECT o FROM Order o WHERE o.company.id = :companyId");

        // Query to get the total count
        StringBuilder countQuery = new StringBuilder("SELECT COUNT(o) FROM Order o WHERE o.company.id = :companyId");

        // Apply filters
        if (params.getFilters() != null && !params.getFilters().isEmpty()) {
            for (int i = 0; i < params.getFilters().size(); i++) {
                var filter = params.getFilters().get(i);
                if ("name".equals(filter.getFieldName())){
                    filter.setFieldName("id");
                }
                query.append(" AND o.").append(filter.getFieldName()).append(" ILIKE :filter").append(i + 1);
                countQuery.append(" AND o.:fieldName ILIKE :filter").append(i + 1);
            }
        }

        // Apply sorting
        if (params.getSorts() != null && !params.getSorts().isEmpty()) {
            query.append(" ORDER BY ");
            for (Sort sort : params.getSorts()) {
                query.append("o.").append(sort.getField()).append(" ").append(sort.getDirection()).append(", ");
            }
            query.setLength(query.length() - 2);
        }

        // Create the query for fetching orders
        Query orderQuery = entityManager.createQuery(query.toString(), Order.class)
                .setParameter("companyId", params.getCompanyId())
                .setFirstResult(params.getPage() * params.getSize())
                .setMaxResults(params.getSize());

        // Create the query for counting total results
        Query countQueryObject = entityManager.createQuery(countQuery.toString())
                .setParameter("companyId", params.getCompanyId());

        // Apply filter parameters dynamically for both queries
        if (params.getFilters() != null && !params.getFilters().isEmpty()) {
            for (int i = 0; i < params.getFilters().size(); i++) {
                var filter = params.getFilters().get(i);
                String filterParam = "%" + filter.getValue() + "%";
                orderQuery.setParameter("filter" + (i + 1), filterParam);
                countQueryObject.setParameter("filter" + (i + 1), filterParam);
            }
        }

        // Fetch the orders
        List<Order> orders = orderQuery.getResultList();

        // Fetch the total count
        long totalCount = (long) countQueryObject.getSingleResult();

        // Return both the results and total count
        return OrdersPagedResult.builder()
                .orderList(OrderMapper.mapToOrderDtoList(orders))
                .totalRecords(totalCount)
                .build();
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