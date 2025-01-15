package com.foodapp.foodapp.order;

import com.foodapp.foodapp.common.SearchParams;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderSearchParams extends SearchParams {
    private List<OrderStatus> statuses;
    private String name;
    private BigDecimal price;
    private List<Long> companyIds;
    private String global;
}
