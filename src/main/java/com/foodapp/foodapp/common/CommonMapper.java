package com.foodapp.foodapp.common;

import com.foodapp.foodapp.administration.company.common.CompanySearchParams;
import com.foodapp.foodapp.administration.company.request.GetCompanyAdministrationRequest;
import com.foodapp.foodapp.administration.userAdministration.UsersSearchParams;
import com.foodapp.foodapp.administration.userAdministration.request.GetUsersAdministrationRequest;
import com.foodapp.foodapp.order.OrderSearchParams;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.request.GetOrdersForCompanyRequest;
import com.foodapp.foodapp.user.Role;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CommonMapper {
    public static SearchParams.SearchParamsBuilder createBaseSearchParams(final SearchParams.SearchParamsBuilder builder,
                                                                          final BasePagedRequest baseRequest) {
        var dateFrom = CommonMapper.getDateFrom(baseRequest.getDateRange(), baseRequest.getDateFrom());
        var dateTo = CommonMapper.getDateTo(baseRequest.getDateRange(), baseRequest.getDateTo());
        return builder
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .sorts(baseRequest.getSorts())
                .page(baseRequest.getPage())
                .size(baseRequest.getSize());
    }

    public static OrderSearchParams mapToSearchParams(final GetOrdersForCompanyRequest request, final List<Long> companyIds) {
        var builder = OrderSearchParams.builder();
        createBaseSearchParams(builder, request);
        return builder
                .statuses(request.getStatuses())
                .companyIds(companyIds)
                .price(request.getPrice())
                .global(request.getGlobalSearch())
                .build();
    }

    public static UsersSearchParams mapToSearchParams(final GetUsersAdministrationRequest request) {
        var builder = UsersSearchParams.builder();
        createBaseSearchParams(builder, request);
        return builder
                .global(request.getGlobalSearch())
                .role(Role.USER)
                .build();
    }

    public static CompanySearchParams mapToSearchParams(final GetCompanyAdministrationRequest request) {
        var builder = CompanySearchParams.builder();
        createBaseSearchParams(builder, request);
        return builder
                .global(request.getGlobalSearch())
                .build();
    }

    public static LocalDate getDateFrom(final DateRange dateRange, LocalDate dateFrom) {
        if (DateRange.LAST_30_DAYS.equals(dateRange)) {
            dateFrom = LocalDate.now().minusDays(30);
        } else if (DateRange.LAST_15_DAYS.equals(dateRange)) {
            dateFrom = LocalDate.now().minusDays(15);
        } else if (DateRange.LAST_7_DAYS.equals(dateRange)) {
            dateFrom = LocalDate.now().minusDays(7);
        }
        return dateFrom;
    }

    public static LocalDate getDateTo(final DateRange dateRange, LocalDate dateTo) {
        if (!DateRange.CUSTOM_DATE_RANGE.equals(dateRange)) {
            return LocalDate.now();
        }
        return dateTo != null ? dateTo : LocalDate.now();
    }

    //    public static void fillFilterSearchParamBuilder(final SearchParams.SearchParamsBuilder builder,
    //                                                                          final List<Filter> filters) {
    //        if (filters == null) {
    //            return;
    //        }
    //        for (var filter : filters) {
    //            if ("status".equals(filter.getFieldName())) {
    //                builder.statuses(CommonMapper.mapToOrderStatuses(filter.getValues()));
    //            } else if ("name".equals(filter.getFieldName())) {
    //                builder.name(CommonUtils.extractNumbers(filter.getValues().get(0)));
    //            } else if ("price".equals(filter.getFieldName())) {
    //                builder.price(BigDecimal.valueOf(Double.parseDouble(filter.getValues().get(0))));
    //            } else if ("deliveryTime".equals(filter.getFieldName())) {
    //                Instant instant = Instant.parse(filter.getValues().get(0));
    //                builder.dateParam(DateParam.builder()
    //                        .date(instant.atZone(ZoneId.systemDefault()).toLocalDate())
    //                        .mode(filter.getMode())
    //                        .build());
    //            } else if ("description".equals(filter.getFieldName())) {
    //                builder.description(filter.getValues().get(0));
    //            } else if ("global".equals(filter.getFieldName())) {
    //                builder.global(filter.getValues().get(0));
    //            }
    //        }
    //    }

    public static List<OrderStatus> mapToOrderStatuses(final List<String> values) {
        return values.stream()
                .map(OrderStatus::valueOf)
                .collect(Collectors.toList());
    }

    public static Sort toSort(List<com.foodapp.foodapp.common.Sort> sorts) {
        var springSort = Sort.unsorted();
        for (var sort : sorts) {
            var direction = getDirection(sort.getDirection());
            springSort = springSort.and(Sort.by(direction, sort.getField()));
        }
        return springSort;
    }

    public static Sort.Direction getDirection(final SortingDirection sortingDirection) {
        return SortingDirection.ASC.equals(sortingDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
    }


}
