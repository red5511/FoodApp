package com.foodapp.foodapp.statistic.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodapp.foodapp.common.DatePeriod;
import com.foodapp.foodapp.common.DateRange;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetStatisticsChartRequest {
    @Setter
    List<Long> companyIds;
    DateRange dateRange;
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateFrom;
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateTo;
    DatePeriod datePeriod;
    Long productId;
    boolean showEarnings;
}
