package com.foodapp.foodapp.statistic.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodapp.foodapp.common.DatePeriod;
import com.foodapp.foodapp.common.DateRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStatisticsChartRequest {
    Long companyId;
    DateRange dateRange;
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateFrom;
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateTo;
    DatePeriod datePeriod;
    Long productId;
    boolean showEarnings;
}
