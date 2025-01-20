package com.foodapp.foodapp.administration.company.sql;

import com.foodapp.foodapp.administration.company.common.CompaniesPagedResult;
import com.foodapp.foodapp.administration.company.common.CompanyMapper;
import com.foodapp.foodapp.administration.company.common.CompanySearchParams;
import com.foodapp.foodapp.user.UserSql;
import io.jsonwebtoken.lang.Collections;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CompanyCustomRepositoryImpl implements CompanyCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CompaniesPagedResult searchOrders(CompanySearchParams params) {
        StringBuilder queryBuilder = new StringBuilder(CompanySql.BASE_QUERY);
        StringBuilder countQueryBuilder = new StringBuilder(CompanySql.BASE_COUNT_QUERY);
        Map<String, Object> valueParamsMap = new HashMap<>();

        queryBuilder.append(CompanySql.DATE_FROM_QUERY);
        queryBuilder.append(CompanySql.DATE_TO_QUERY);
        countQueryBuilder.append(CompanySql.DATE_FROM_QUERY);
        countQueryBuilder.append(CompanySql.DATE_TO_QUERY);
        valueParamsMap.put(CompanySql.DATE_TO_PARAM, params.getDateTo() != null ? params.getDateTo().atTime(23, 59, 59) : null);
        valueParamsMap.put(CompanySql.DATE_FROM_PARAM, params.getDateFrom() != null ? params.getDateFrom().atStartOfDay() : null);

        if (StringUtils.isNotEmpty(params.getGlobal())) {
            queryBuilder.append(CompanySql.GLOBAL_QUERY);
            countQueryBuilder.append(CompanySql.GLOBAL_QUERY);
            valueParamsMap.put(CompanySql.GLOBAL_PARAM, params.getGlobal());
        }

        if (!Collections.isEmpty(params.getSorts())) {
            StringBuilder sortQueryBuilder = new StringBuilder();
            sortQueryBuilder.append(CompanySql.BASE_SORT_QUERY);
            for (var sort : params.getSorts()) {
                if ("usersAmount".equals(sort.getField())) {
                    // Handle sorting by the number of companies
                    sortQueryBuilder.append(" ")
                            .append(CompanySql.USERS_AMOUNT_SORT_QUERY)
                            .append(" ")
                            .append(sort.getDirection().name());
                } else {
                    sortQueryBuilder.append(" c.")
                            .append(sort.getField())
                            .append(" ")
                            .append(sort.getDirection().name());
                }
            }
            queryBuilder.append(sortQueryBuilder.toString());
        }

        Query companyQuery = entityManager.createQuery(queryBuilder.toString(), Company.class)
                .setFirstResult(params.getPage() * params.getSize())
                .setMaxResults(params.getSize());

        // Create the query for counting total results
        Query countQuery = entityManager.createQuery(countQueryBuilder.toString());

        valueParamsMap.forEach((param, value) -> {
            companyQuery.setParameter(param, value);
            countQuery.setParameter(param, value);
        });

        // Fetch the orders
        List<Company> companies = companyQuery.getResultList();

        // Fetch the total count
        long totalCount = (long) countQuery.getSingleResult();

        return CompaniesPagedResult.builder()
                .companies(CompanyMapper.toCompaniesDto(companies))
                .totalRecords(totalCount)
                .build();
    }
}
