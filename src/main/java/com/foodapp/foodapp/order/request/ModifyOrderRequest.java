package com.foodapp.foodapp.order.request;

import com.foodapp.foodapp.order.dto.OrderDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModifyOrderRequest {
    private OrderDto order;
    private Long modifiedOrderIf;
}
