package com.foodapp.foodapp.order;

import com.foodapp.foodapp.common.OrdersPagedResult;
import com.foodapp.foodapp.common.SearchParams;

public interface OrderRepositoryCustom {
    OrdersPagedResult searchOrders(SearchParams params);
}