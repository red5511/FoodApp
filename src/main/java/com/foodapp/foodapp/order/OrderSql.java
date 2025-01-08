package com.foodapp.foodapp.order;

public class OrderSql {
    public static final String COMPANY_ID_PARAM = "companyId";
    public static final String COMPANY_IDS_PARAM = "companyIds";
    public static final String DELIVERY_DATE_PARAM = "deliveryTime";
    public static final String DESCRIPTION_PARAM = "description";
    public static final String PRICE_PARAM = "price";
    public static final String STATUSES_PARAM = "statuses";
    public static final String CREATED_DATE_PARAM = "createdDate";
    public static final String GLOBAL_PARAM = "global";
    public static final String DATE_FROM_PARAM = "dateFrom";
    public static final String DATE_TO_PARAM = "dateTo";

    public static final String BASE_QUERY = "SELECT o FROM Order o WHERE o.company.id IN (:" + COMPANY_IDS_PARAM + ")";
    public static final String BASE_SORT_QUERY = " ORDER BY";
    public static final String BASE_COUNT_QUERY = "SELECT COUNT(o) FROM Order o WHERE o.company.id IN (:" + COMPANY_IDS_PARAM + ")";
    public static final String DATE_IS_BETWEEN_QUERY =
            " AND DATE(o.deliveryTime) BETWEEN :" + DATE_FROM_PARAM + " AND :" + DATE_TO_PARAM;
    public static final String DATE_IS_QUERY = " AND :" + DELIVERY_DATE_PARAM + " = DATE(o.deliveryTime)";
    public static final String DATE_IS_NOT_QUERY = " AND :" + DELIVERY_DATE_PARAM + " != DATE(o.deliveryTime)";
    public static final String DATE_BEFORE_QUERY = " AND :" + DELIVERY_DATE_PARAM + " > DATE(o.deliveryTime)";
    public static final String DATE_AFTER_QUERY = " AND :" + DELIVERY_DATE_PARAM + " < DATE(o.deliveryTime)";
    public static final String DESCRIPTION_QUERY = " AND o.description LIKE CONCAT('%', :" + DESCRIPTION_PARAM + ", '%')";
    public static final String PRICE_QUERY = " AND o.price = :" + PRICE_PARAM;
    public static final String STATUSES_QUERY = " AND o.status IN (:" + STATUSES_PARAM + ")";
    public static final String GLOBAL_QUERY = " AND (CAST(o.id AS text) LIKE CONCAT('%', :" + GLOBAL_PARAM + ", '%') " +
            " OR o.description LIKE CONCAT('%', :" + GLOBAL_PARAM + ", '%') " +
            " OR CAST(o.price AS text) LIKE CONCAT('%', :" + GLOBAL_PARAM + ", '%')) ";
    //" OR CAST(o.deliveryTime AS text) LIKE CONCAT('%', :" + GLOBAL_PARAM + ", '%'))";


    public static final String DATE_IS_MODE = "dateIs";
    public static final String DATE_IS_NOT_MODE = "dateIsNot";
    public static final String DATE_BEFORE_MODE = "dateBefore";
    public static final String DATE_AFTER_MODE = "dateAfter";
}
