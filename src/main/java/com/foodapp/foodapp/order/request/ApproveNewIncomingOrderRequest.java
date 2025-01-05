package com.foodapp.foodapp.order.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApproveNewIncomingOrderRequest {
    @Schema(required = true)
    private Long companyId;
    @Schema(required = true)
    private Long orderId;
}
