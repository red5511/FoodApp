package com.foodapp.foodapp.statistic.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStatisticsChartResponse {
    private List<Long> data;
    private List<String> labels;
}
