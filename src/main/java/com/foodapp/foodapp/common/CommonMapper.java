package com.foodapp.foodapp.common;

import com.foodapp.foodapp.order.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class CommonMapper {
    public static SearchParams mapToSearchParams(final List<Filter> filters, final int pageSize, final int page,
                                                 final Long companyId, final List<Sort> sorts) {
        var builder = SearchParams.builder();
        fillFilterSearchParamBuilder(builder, filters);
        return builder
                .page(page)
                .size(pageSize)
                .companyId(companyId)
                .sorts(sorts)
                .build();
    }

    public static void fillFilterSearchParamBuilder(final SearchParams.SearchParamsBuilder builder,
                                                                          final List<Filter> filters) {
        if (filters == null) {
            return;
        }
        for (var filter : filters) {
            if ("status".equals(filter.getFieldName())) {
                builder.statuses(CommonMapper.mapToOrderStatuses(filter.getValues()));
            } else if ("name".equals(filter.getFieldName())) {
                builder.name(CommonUtils.extractNumbers(filter.getValues().get(0)));
            } else if ("price".equals(filter.getFieldName())) {
                builder.price(BigDecimal.valueOf(Double.parseDouble(filter.getValues().get(0))));
            } else if ("deliveryTime".equals(filter.getFieldName())) {
                Instant instant = Instant.parse(filter.getValues().get(0));
                builder.dateParam(DateParam.builder()
                        .date(instant.atZone(ZoneId.systemDefault()).toLocalDate())
                        .mode(filter.getMode())
                        .build());
            } else if ("description".equals(filter.getFieldName())) {
                builder.description(filter.getValues().get(0));
            } else if ("global".equals(filter.getFieldName())) {
                builder.global(filter.getValues().get(0));
            }
        }
    }

    public static List<OrderStatus> mapToOrderStatuses(final List<String> values) {
        return values.stream()
                .map(OrderStatus::valueOf)
                .collect(Collectors.toList());
    }
}
