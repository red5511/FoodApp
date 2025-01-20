package com.foodapp.foodapp.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ProductsPagedResult searchProducts(ProductSearchParams params) {
        StringBuilder queryBuilder = new StringBuilder(ProductSql.BASE_QUERY);
        StringBuilder countQueryBuilder = new StringBuilder(ProductSql.BASE_COUNT_QUERY);
        Map<String, Object> valueParamsMap = new HashMap<>();

        if(StringUtils.isNotEmpty(params.getGlobal())) {
            queryBuilder.append(ProductSql.GLOBAL_QUERY);
            countQueryBuilder.append(ProductSql.GLOBAL_QUERY);
            valueParamsMap.put(ProductSql.GLOBAL_PARAM, params.getGlobal());
        }

        if(!Collections.isEmpty(params.getSorts())) {
            StringBuilder sortQueryBuilder = new StringBuilder();
            sortQueryBuilder.append(ProductSql.BASE_SORT_QUERY);
            for(var sort : params.getSorts()) {
                // Handle regular field sorting
                sortQueryBuilder.append(" p.")
                                .append(sort.getField())
                                .append(" ")
                                .append(sort.getDirection().name());
            }
            queryBuilder.append(sortQueryBuilder.toString());
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), Product.class)
                                   .setParameter(ProductSql.COMPANY_IDS_PARAM, List.of(params.getCompanyId()))
                                   .setFirstResult(params.getPage() * params.getSize())
                                   .setMaxResults(params.getSize());

        // Create the query for counting total results
        Query countQuery = entityManager.createQuery(countQueryBuilder.toString())
                                        .setParameter(ProductSql.COMPANY_IDS_PARAM, List.of(params.getCompanyId()));

        valueParamsMap.forEach((param, value) -> {
            query.setParameter(param, value);
            countQuery.setParameter(param, value);
        });

        List<Product> products = query.getResultList();

        long totalCount = (long) countQuery.getSingleResult();

        return ProductsPagedResult.builder()
                                  .products(ProductMapper.mapToProductsDto(products))
                                  .totalRecords(totalCount)
                                  .build();
    }
}
