package com.foodapp.foodapp.order.request;

import com.foodapp.foodapp.common.BasePagedRequest;
import com.foodapp.foodapp.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Data
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetOrdersForCompanyRequest extends BasePagedRequest {
    private Long validatableCompanyId;
    private List<Long> companyIds;
    private List<OrderStatus> statuses;
    private BigDecimal price;
    private String globalSearch;
}
