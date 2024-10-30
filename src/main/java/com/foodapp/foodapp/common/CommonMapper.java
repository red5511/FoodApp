package com.foodapp.foodapp.common;

import com.foodapp.foodapp.order.OrderStatus;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class CommonMapper {
    public static SearchParams mapToSearchParams(final List<Filter> filters, final int pageSize, final int page,
                                                 final Long companyId) {
        var builder = fillSearchParamBuilder(SearchParams.builder(), filters);
        return builder.page(page)
                .size(pageSize)
                .companyId(companyId)
                .build();

    }

    public static SearchParams.SearchParamsBuilder fillSearchParamBuilder(final SearchParams.SearchParamsBuilder builder,
                                                                          final List<Filter> filters) {
        for (var filter : filters) {
            if ("status".equals(filter.getFieldName())) {
                builder.statuses(CommonMapper.mapToOrderStatuses(filter.getValues()));
            } else if ("name".equals(filter.getFieldName())) {
                builder.name(CommonUtils.extractNumbers(filter.getValues().get(0)));
            } else if ("price".equals(filter.getFieldName())) {
                builder.price(CommonUtils.extractNumbers(filter.getValues().get(0)));
            } else if ("date".equals(filter.getFieldName())) {
                Instant instant = Instant.parse(filter.getValues().get(0));
                builder.dateParam(DateParam.builder()
                        .date(instant.atZone(ZoneId.systemDefault()).toLocalDate())
                        .mode(filter.getMode())
                        .build());
            } else if ("description".equals(filter.getFieldName())) {
                builder.description(filter.getValues().get(0));
            }
        }
        return builder;

    }

    public static List<OrderStatus> mapToOrderStatuses(final List<String> values) {
        return values.stream()
                .map(OrderStatus::valueOf)
                .collect(Collectors.toList());
    }
}
