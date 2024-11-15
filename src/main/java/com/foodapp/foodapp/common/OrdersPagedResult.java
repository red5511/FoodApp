package com.foodapp.foodapp.common;

import java.util.List;

import com.foodapp.foodapp.order.dto.OrderDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrdersPagedResult {
    private List<OrderDto> orderList;
    private long totalRecords;
}
