package com.foodapp.foodapp.statistic.request;

import com.foodapp.foodapp.common.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStatisticsOrderCountRequest {
    DateRange dateRange;
}
