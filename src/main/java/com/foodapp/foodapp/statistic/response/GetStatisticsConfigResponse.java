package com.foodapp.foodapp.statistic.response;

import com.foodapp.foodapp.common.DatePeriod;
import com.foodapp.foodapp.common.DateRange;
import com.foodapp.foodapp.product.ProductDto;
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
    List<DateRange> dateRanges;
    List<ProductDto> products;
    List<DatePeriod> datePeriods;
}
