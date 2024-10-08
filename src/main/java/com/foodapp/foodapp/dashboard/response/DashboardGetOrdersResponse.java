package com.foodapp.foodapp.dashboard.response;

import com.foodapp.foodapp.order.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGetOrdersResponse {
    private List<OrderDto> orderList;
}
