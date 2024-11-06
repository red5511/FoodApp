package com.foodapp.foodapp.common;

import com.foodapp.foodapp.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class SearchParams {
    private List<OrderStatus> statuses;
    private String name;
    private BigDecimal price;
    private String description;
    private DateParam dateParam;
    private List<Sort> sorts;
    private int page;
    private int size;
    private Long companyId;
    private String global;
}
