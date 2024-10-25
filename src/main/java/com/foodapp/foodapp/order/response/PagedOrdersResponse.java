package com.foodapp.foodapp.order.response;

import com.foodapp.foodapp.common.OrdersPagedResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagedOrdersResponse {
    private OrdersPagedResult pagedResult;
}
