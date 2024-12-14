package com.foodapp.foodapp.order.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodapp.foodapp.common.DateRange;
import com.foodapp.foodapp.common.Filter;
import com.foodapp.foodapp.common.Sort;
import com.foodapp.foodapp.order.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetOrdersForCompanyRequest {
    private Long validatableCompanyId;
    private List<Sort> sorts;
    private int page;
    private int size;
    private List<Long> companyIds;
    private List<OrderStatus> statuses;
    private BigDecimal price;
    private DateRange dateRange;
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateFrom;
    @JsonFormat(pattern = "d.MM.yyyy")
    LocalDate dateTo;
    private String globalSearch;
}
