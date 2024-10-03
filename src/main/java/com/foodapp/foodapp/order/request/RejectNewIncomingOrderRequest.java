package com.foodapp.foodapp.order.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RejectNewIncomingOrderRequest {
    private Long companyId;
    private Long orderId;
}
