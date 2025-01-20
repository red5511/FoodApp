package com.foodapp.foodapp.administration.company.sql;

public class CompanySql {
    public static final String GLOBAL_PARAM = "global";
    public static final String DATE_FROM_PARAM = "dateFrom";
    public static final String DATE_TO_PARAM = "dateTo";

    public static final String WHERE_QUERY = " WHERE";
    public static final String BASE_QUERY = "SELECT c FROM Company c" + WHERE_QUERY;
    public static final String BASE_COUNT_QUERY = "SELECT COUNT(c) FROM Company c" + WHERE_QUERY;

    public static final String DATE_FROM_QUERY =
            "  (CAST(:" + DATE_FROM_PARAM + " AS TIMESTAMP) IS NULL OR c.createdDate >= :" + DATE_FROM_PARAM + ")";

    public static final String DATE_TO_QUERY =
            " AND (CAST(:" + DATE_TO_PARAM + " AS TIMESTAMP) IS NULL OR c.createdDate <= :" + DATE_TO_PARAM + ")";

    public static final String GLOBAL_QUERY = " AND (CAST(c.id AS text) = :" + GLOBAL_PARAM +
            " OR LOWER(c.name) LIKE CONCAT('%', LOWER(:" + GLOBAL_PARAM + "), '%'))";

    public static final String BASE_SORT_QUERY = " ORDER BY";
    public static final String USERS_AMOUNT_SORT_QUERY = " SIZE(c.users)";
}
