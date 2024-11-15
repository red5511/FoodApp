package com.foodapp.foodapp.statistic;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.foodapp.foodapp.common.DatePeriod;
import com.foodapp.foodapp.common.DateRange;
import com.foodapp.foodapp.order.OrderRepository;
import com.foodapp.foodapp.product.ProductMapper;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.statistic.request.GetStatisticsChartRequest;
import com.foodapp.foodapp.statistic.response.GetStatisticsChartResponse;
import com.foodapp.foodapp.statistic.response.GetStatisticsConfigResponse;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StatisticsService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public GetStatisticsChartResponse getStatisticsChart(final GetStatisticsChartRequest request) {
        var dateFrom = getDateFrom(request.getDateRange(), request.getDateFrom());
        var dateTo = getDateTo(request.getDateRange(), request.getDateTo());
        List<Object[]> result = getOrderStatisticsChart(dateFrom, dateTo, request);

        Map<LocalDate, Long> dateOrderCountMap = result.stream()
                                                       .collect(Collectors.toMap(
                                                           row -> ((LocalDateTime) row[1]).toLocalDate(),
                                                           row -> (Long) row[0]
                                                       ));

        List<Long> data = new ArrayList<>();
        List<String> labels = new ArrayList<>();


        List<LocalDate> allDates = getAllDates(dateFrom, dateTo, request.getDatePeriod());

        for(LocalDate date : allDates) {
            Long count = dateOrderCountMap.getOrDefault(date, 0L);
            data.add(count);
            labels.add(StatisticsMapper.dateToLabel(date, request.getDatePeriod()));
        }

        return GetStatisticsChartResponse.builder()
                                         .data(data)
                                         .labels(labels)
                                         .build();
    }

    private List<Object[]> getOrderStatisticsChart(final LocalDate dateFrom, final LocalDate dateTo, final GetStatisticsChartRequest request) {
        if(request.getProductId() == null) {
            return orderRepository.getOrderStatisticsChart(request.getCompanyId(),
                                                           request.getDatePeriod().name().toLowerCase(),
                                                           dateFrom.atStartOfDay(),
                                                           dateTo.atTime(23, 59)
            );
        }
        return orderRepository.getOrderStatisticsChartByProductId(request.getCompanyId(),
                                                                  request.getDatePeriod().name().toLowerCase(),
                                                                  dateFrom.atStartOfDay(),
                                                                  dateTo.atTime(23, 59),
                                                                  request.getProductId()
        );
    }

    private List<LocalDate> getAllDates(final LocalDate dateFrom, final LocalDate dateTo, final DatePeriod datePeriod) {
        if(DatePeriod.DAY.equals(datePeriod)) {
            return dateFrom.datesUntil(dateTo).toList();
        }
        else if(DatePeriod.WEEK.equals(datePeriod)) {
            List<LocalDate> weeks = new ArrayList<>();
            LocalDate current = dateFrom.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            while(!current.isAfter(dateTo)) {
                weeks.add(current);
                current = current.plusWeeks(1);
            }
            return weeks;
        }
        else if(DatePeriod.MONTH.equals(datePeriod)) {
            List<LocalDate> months = new ArrayList<>();
            LocalDate current = dateFrom.with(TemporalAdjusters.firstDayOfMonth());
            while(!current.isAfter(dateTo)) {
                months.add(current);
                current = current.plusMonths(1);
            }
            return months;
        }
        return List.of();
    }

    public GetStatisticsConfigResponse getStatisticsConfig(final List<Long> companyIds) {
        var products = productRepository.findAllByCompanyIdIn(companyIds);
        return GetStatisticsConfigResponse.builder()
                                          .datePeriodModels(StatisticsMapper.getDataPeriodModels())
                                          .dataRangeModels(StatisticsMapper.getDataRangeModels())
                                          .products(ProductMapper.mapToProductsDto(products))
                                          .build();
    }

    private LocalDate getDateFrom(final DateRange dateRange, LocalDate dateFrom) {
        if(DateRange.LAST_30_DAYS.equals(dateRange)) {
            dateFrom = LocalDate.now().minusDays(30);
        }
        else if(DateRange.LAST_15_DAYS.equals(dateRange)) {
            dateFrom = LocalDate.now().minusDays(15);
        }
        else if(DateRange.LAST_7_DAYS.equals(dateRange)) {
            dateFrom = LocalDate.now().minusDays(7);
        }
        return dateFrom;
    }

    private LocalDate getDateTo(final DateRange dateRange, LocalDate dateTo) {
        if(!DateRange.CUSTOM_DATE_RANGE.equals(dateRange)) {
            return LocalDate.now().plusDays(1);
        }
        return dateTo != null ? dateTo.plusDays(1) : LocalDate.now().plusDays(1);
    }
}
