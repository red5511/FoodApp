package com.foodapp.foodapp.statistic.response;

import com.foodapp.foodapp.common.DatePeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DatePeriodModel {
    DatePeriod datePeriod;
    String translatedValue;
}
