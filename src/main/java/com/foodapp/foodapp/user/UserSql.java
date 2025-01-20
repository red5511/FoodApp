package com.foodapp.foodapp.user;

public class UserSql {
    public static final String GLOBAL_PARAM = "global";
    public static final String DATE_FROM_PARAM = "dateFrom";
    public static final String DATE_TO_PARAM = "dateTo";
    public static final String ROLE_PARAM = "role";

    public static final String BASE_QUERY = "SELECT u FROM User u";
    public static final String ROLE_USER_QUERY = " WHERE u.role = :" + ROLE_PARAM;
    public static final String BASE_COUNT_QUERY = "SELECT COUNT(u) FROM User u";

    public static final String DATE_IS_BETWEEN_QUERY =
            " AND DATE(u.createdDate) BETWEEN :" + DATE_FROM_PARAM + " AND :" + DATE_TO_PARAM;

    public static final String DATE_FROM_QUERY =
            " AND (CAST(:" + DATE_FROM_PARAM + " AS TIMESTAMP) IS NULL OR u.createdDate >= :" + DATE_FROM_PARAM + ") ";

    public static final String DATE_TO_QUERY =
            " AND (CAST(:" + DATE_TO_PARAM + " AS TIMESTAMP) IS NULL OR u.createdDate <= :" + DATE_TO_PARAM + ") ";

    public static final String GLOBAL_QUERY = " AND LOWER(u.email) LIKE CONCAT('%', LOWER(:" + GLOBAL_PARAM + "), '%') " +
            " OR LOWER(u.firstName) LIKE CONCAT('%', LOWER(:" + GLOBAL_PARAM + "), '%') " +
            " OR LOWER(u.lastName) LIKE CONCAT('%', LOWER(:" + GLOBAL_PARAM + "), '%') " +
            " OR LOWER(u.phoneNumber) LIKE CONCAT('%', LOWER(:" + GLOBAL_PARAM + "), '%') ";


    public static final String BASE_SORT_QUERY = " ORDER BY";
    public static final String COMPANY_AMOUNT_SORT_QUERY = "SIZE(u.companies)";



}
