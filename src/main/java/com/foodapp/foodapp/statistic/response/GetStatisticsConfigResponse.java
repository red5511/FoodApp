package com.foodapp.foodapp.statistic.response;

import com.foodapp.foodapp.product.ProductDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStatisticsConfigResponse {
    @Schema(description = "List of date range models", required = true) //dzieki temu nie mamy tej zminnej oznaczonej jako Optional
    List<DateRangeModel> dataRangeModels;
    @Schema(description = "List of products", required = true)
    List<ProductDto> products;
    @Schema(description = "List of date period models", required = true)
    List<DatePeriodModel> datePeriodModels;
}
