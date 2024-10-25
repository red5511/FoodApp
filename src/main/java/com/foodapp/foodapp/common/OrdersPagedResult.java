package com.foodapp.foodapp.common;

import com.foodapp.foodapp.order.OrderDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class OrdersPagedResult {
    private List<OrderDto> orderList;
    private long totalRecords;
}
