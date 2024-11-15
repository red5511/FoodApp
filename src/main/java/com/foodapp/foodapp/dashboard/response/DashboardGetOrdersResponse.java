package com.foodapp.foodapp.dashboard.response;

import java.util.List;

import com.foodapp.foodapp.order.dto.OrderDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGetOrdersResponse {
    private List<OrderDto> orderList;
}
