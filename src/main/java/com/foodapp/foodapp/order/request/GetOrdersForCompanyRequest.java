package com.foodapp.foodapp.order.request;

import com.foodapp.foodapp.common.Filter;
import com.foodapp.foodapp.common.Sort;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetOrdersForCompanyRequest {
    List<Filter> filters;
    List<Sort> sorts;
    int page;
    int size;
    private Long companyId;
}
