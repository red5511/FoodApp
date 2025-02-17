package com.foodapp.foodapp.statistic;

import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.common.DatePeriod;
import com.foodapp.foodapp.order.OrderRepository;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.product.ProductMapper;
import com.foodapp.foodapp.product.ProductRepository;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.statistic.request.GetStatisticsChartRequest;
import com.foodapp.foodapp.statistic.response.GetStatisticsChartResponse;
import com.foodapp.foodapp.statistic.response.GetStatisticsConfigResponse;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class StatisticsService {
    public static List<OrderStatus> STATISTICS_ORDER_STATUSES_TO_EXCLUDE = List.of(OrderStatus.MODIFIED, OrderStatus.REJECTED);
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ContextProvider contextProvider;

    public GetStatisticsChartResponse getStatisticsChart(final GetStatisticsChartRequest request) {
        request.setCompanyIds(contextProvider.getCompanyIdsWithHolding(request.getCompanyIds()));

        var dateFrom = CommonMapper.getDateFrom(request.getDateRange(), request.getDateFrom());
        var dateTo = CommonMapper.getDateTo(request.getDateRange(), request.getDateTo());

        List<Long> ordersCount = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        List<BigDecimal> earnings = new ArrayList<>();

        if (!request.isShowEarnings()) {
            calculateStatistics(ordersCount, labels, dateFrom, dateTo, request);
        } else {
            calculateStatisticsWithEarnings(ordersCount, labels, earnings, dateFrom, dateTo, request);
        }

        var earningsTotal = earnings.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        var ordersCountTotal = ordersCount.stream()
                .mapToLong(Long::longValue)
                .sum();

        return GetStatisticsChartResponse.builder()
                .ordersCount(ordersCount)
                .labels(labels)
                .earnings(earnings)
                .earningsTotal(earningsTotal)
                .ordersCountTotal(ordersCountTotal)
                .build();
    }

    private void calculateStatisticsWithEarnings(final List<Long> ordersCount,
                                                 final List<String> labels,
                                                 final List<BigDecimal> earnings,
                                                 final LocalDate dateFrom,
                                                 final LocalDate dateTo,
                                                 final GetStatisticsChartRequest request) {
        List<Object[]> result = getOrderStatisticsChartWithEarningsFromRepository(dateFrom, dateTo, request);
        Map<LocalDate, Long> dateOrderCountMap = new HashMap<>();
        Map<LocalDate, BigDecimal> earningsOrderCountMap = new HashMap<>();

        result.forEach(row -> {
            dateOrderCountMap.put(((LocalDateTime) row[2]).toLocalDate(), (Long) row[0]);
            earningsOrderCountMap.put(((LocalDateTime) row[2]).toLocalDate(), (BigDecimal) row[1]);
        });
        List<LocalDate> allDates = getAllDates(dateFrom, dateTo, request.getDatePeriod());

        for (LocalDate date : allDates) {
            Long count = dateOrderCountMap.getOrDefault(date, 0L);
            ordersCount.add(count);
            labels.add(StatisticsMapper.dateToLabel(date, request.getDatePeriod()));
            BigDecimal earn = earningsOrderCountMap.getOrDefault(date, BigDecimal.ZERO);
            earnings.add(earn);
        }
    }

    private List<Object[]> getOrderStatisticsChartWithEarningsFromRepository(final LocalDate dateFrom,
                                                                             final LocalDate dateTo,
                                                                             final GetStatisticsChartRequest request) {
        if (request.getProductId() == null) {
            return orderRepository.getOrderStatisticsChartWithEarnings(request.getCompanyIds(),
                    request.getDatePeriod().name().toLowerCase(),
                    dateFrom.atStartOfDay(),
                    dateTo.atTime(23, 59),
                    STATISTICS_ORDER_STATUSES_TO_EXCLUDE
            );
        }
        return orderRepository.getOrderStatisticsChartByProductIdWithEarnings(request.getCompanyIds(),
                request.getDatePeriod().name().toLowerCase(),
                dateFrom.atStartOfDay(),
                dateTo.atTime(23, 59),
                request.getProductId(),
                STATISTICS_ORDER_STATUSES_TO_EXCLUDE
        );
    }

    private void calculateStatistics(final List<Long> ordersCount,
                                     final List<String> labels,
                                     final LocalDate dateFrom,
                                     final LocalDate dateTo,
                                     final GetStatisticsChartRequest request) {
        List<Object[]> result = getOrderStatisticsChartFromRepository(dateFrom, dateTo, request);
        Map<LocalDate, Long> dateOrderCountMap = result.stream()
                .collect(Collectors.toMap(
                        row -> ((LocalDateTime) row[1]).toLocalDate(),
                        row -> (Long) row[0]
                ));

        List<LocalDate> allDates = getAllDates(dateFrom, dateTo, request.getDatePeriod());

        for (LocalDate date : allDates) {
            Long count = dateOrderCountMap.getOrDefault(date, 0L);
            ordersCount.add(count);
            labels.add(StatisticsMapper.dateToLabel(date, request.getDatePeriod()));
        }
    }

    private List<Object[]> getOrderStatisticsChartFromRepository(final LocalDate dateFrom, final LocalDate dateTo,
                                                                 final GetStatisticsChartRequest request) {
        if (request.getProductId() == null) {
            return orderRepository.getOrderStatisticsChart(request.getCompanyIds(),
                    request.getDatePeriod().name().toLowerCase(),
                    dateFrom.atStartOfDay(),
                    dateTo.atTime(23, 59),
                    STATISTICS_ORDER_STATUSES_TO_EXCLUDE
            );
        }
        return orderRepository.getOrderStatisticsChartByProductId(request.getCompanyIds(),
                request.getDatePeriod().name().toLowerCase(),
                dateFrom.atStartOfDay(),
                dateTo.atTime(23, 59),
                request.getProductId(),
                STATISTICS_ORDER_STATUSES_TO_EXCLUDE
        );
    }

    private List<LocalDate> getAllDates(final LocalDate dateFrom, LocalDate dateTo, final DatePeriod datePeriod) {
        dateTo = dateTo.plusDays(1);
        if (DatePeriod.DAY.equals(datePeriod)) {
            return dateFrom.datesUntil(dateTo).toList();
        } else if (DatePeriod.WEEK.equals(datePeriod)) {
            List<LocalDate> weeks = new ArrayList<>();
            LocalDate current = dateFrom.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            while (!current.isAfter(dateTo)) {
                weeks.add(current);
                current = current.plusWeeks(1);
            }
            return weeks;
        } else if (DatePeriod.MONTH.equals(datePeriod)) {
            List<LocalDate> months = new ArrayList<>();
            LocalDate current = dateFrom.with(TemporalAdjusters.firstDayOfMonth());
            while (!current.isAfter(dateTo)) {
                months.add(current);
                current = current.plusMonths(1);
            }
            return months;
        }
        return List.of();
    }

    public GetStatisticsConfigResponse getStatisticsConfig(final Long companyId) {
        var companyIds = contextProvider.getCompanyIdsWithHolding(List.of(companyId));
        var products = productRepository.findAllByCompanyIdIn(companyIds);
        return GetStatisticsConfigResponse.builder()
                .datePeriodModels(StatisticsMapper.getDataPeriodModels())
                .dataRangeModels(StatisticsMapper.getDataRangeModels())
                .products(ProductMapper.mapToProductsDto(products))
                .build();
    }
}
