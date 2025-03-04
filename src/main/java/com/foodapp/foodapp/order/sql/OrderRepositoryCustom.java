package com.foodapp.foodapp.order.sql;

import com.foodapp.foodapp.common.OrdersPagedResult;

public interface OrderRepositoryCustom {
    OrdersPagedResult searchOrders(OrderSearchParams params);
}