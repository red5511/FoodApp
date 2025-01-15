package com.foodapp.foodapp.order;

import com.foodapp.foodapp.common.OrdersPagedResult;

public interface OrderRepositoryCustom {
    OrdersPagedResult searchOrders(OrderSearchParams params);
}