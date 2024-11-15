package com.foodapp.foodapp.statistic.response;

import com.foodapp.foodapp.common.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRangeModel {
    DateRange dateRange;
    String translatedValue;
}
