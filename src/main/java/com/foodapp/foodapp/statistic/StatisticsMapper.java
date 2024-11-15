package com.foodapp.foodapp.statistic;

import com.foodapp.foodapp.common.DatePeriod;
import com.foodapp.foodapp.common.DateRange;
import com.foodapp.foodapp.statistic.response.DatePeriodModel;
import com.foodapp.foodapp.statistic.response.DateRangeModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foodapp.foodapp.common.DatePeriod.*;
import static com.foodapp.foodapp.common.DateRange.*;

public class StatisticsMapper {
    public static Map<DatePeriod, String> TRANSLATED_VALUES_DATE_PERIOD_MAP = Map.of(
            DAY, "Dzień",
            WEEK, "Tydzień",
            MONTH, "Miesiąc");

    public static Map<DateRange, String> TRANSLATED_VALUES_DATE_RANGE_MAP = Map.of(
            LAST_30_DAYS, "Ostatnie 30 dni",
            LAST_15_DAYS, "Ostatnie 15 dni",
            LAST_7_DAYS, "Ostatnie 7 dni",
            CUSTOM_DATE_RANGE, "Własny przedział");

    public static List<DateRangeModel> getDataRangeModels() {
        var dateRanges = Arrays.asList(DateRange.values());
        return dateRanges.stream()
                .map(dateRange -> DateRangeModel.builder()
                        .dateRange(dateRange)
                        .translatedValue(TRANSLATED_VALUES_DATE_RANGE_MAP.get(dateRange))
                        .build())
                .collect(Collectors.toList());
    }

    public static List<DatePeriodModel> getDataPeriodModels() {
        var datePeriods = Arrays.asList(DatePeriod.values());
        return datePeriods.stream()
                .map(datePeriod -> DatePeriodModel.builder()
                        .datePeriod(datePeriod)
                        .translatedValue(TRANSLATED_VALUES_DATE_PERIOD_MAP.get(datePeriod))
                        .build())
                .collect(Collectors.toList());
    }

    public static String dateToLabel(final LocalDate date, final DatePeriod datePeriod) {
        if (DAY.equals(datePeriod)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM.dd");
            return date.format(formatter);
        }
        return date.toString();
    }
}
