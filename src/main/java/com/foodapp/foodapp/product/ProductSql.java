package com.foodapp.foodapp.product;

public class ProductSql {
    public static final String GLOBAL_PARAM = "global";
    public static final String COMPANY_IDS_PARAM = "companyIds";


    public static final String BASE_QUERY = "SELECT p FROM Product p WHERE p.company.id IN (:" + COMPANY_IDS_PARAM + ")";
    public static final String BASE_COUNT_QUERY = "SELECT COUNT(p) FROM Product p WHERE p.company.id IN (:" + COMPANY_IDS_PARAM + ")";

    public static final String GLOBAL_QUERY = " AND (LOWER(p.name) LIKE CONCAT('%', LOWER(:" + GLOBAL_PARAM + "), '%') " +
                                              " OR LOWER(p.description) LIKE CONCAT('%', LOWER(:" + GLOBAL_PARAM + "), '%') " +
                                              " OR CAST(p.price AS text) LIKE CONCAT('%', :" + GLOBAL_PARAM + ", '%')) ";


    public static final String BASE_SORT_QUERY = " ORDER BY";
}
