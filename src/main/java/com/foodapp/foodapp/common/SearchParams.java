package com.foodapp.foodapp.common;

import com.foodapp.foodapp.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
public class SearchParams {
    private List<OrderStatus> statuses;
    private String name;
    private BigDecimal price;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private List<Sort> sorts;
    private int page;
    private int size;
    private List<Long> companyIds;
    private String global;
}
