package com.foodapp.foodapp.order.response;

import java.util.List;
import java.util.Map;

import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.Severity;

import com.foodapp.foodapp.statistic.response.DateRangeModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersConfigResponse {
    @Schema(description = "List of status models", required = true)
    List<OrderStatusModel> orderStatusModels;
    @Schema(description = "Map of severity", required = true)
    Map<OrderStatus, Severity> statusSeverityMap;
    @Schema(description = "List of date range models", required = true)
    List<DateRangeModel> dataRangeModels;
}
