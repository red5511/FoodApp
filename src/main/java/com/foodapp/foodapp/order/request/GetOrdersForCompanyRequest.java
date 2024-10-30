package com.foodapp.foodapp.order.request;

import com.foodapp.foodapp.common.Filter;
import com.foodapp.foodapp.common.SearchParamsInterface;
import com.foodapp.foodapp.common.Sort;
import com.foodapp.foodapp.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersForCompanyRequest implements SearchParamsInterface {
//    private List<OrderStatus> status;
//    private String name;
//    private BigDecimal price;
//    private String description;
    List<Filter> filters;
    List<Sort> sorts;
    int page;
    int size;
    private Long companyId;
}
