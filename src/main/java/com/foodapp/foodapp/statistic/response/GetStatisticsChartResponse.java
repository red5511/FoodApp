package com.foodapp.foodapp.statistic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStatisticsChartResponse {
    private List<Long> ordersCount;
    private List<String> labels;
    private List<BigDecimal> earnings;
    private Long ordersCountTotal;
    private BigDecimal earningsTotal;
}
