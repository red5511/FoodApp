package com.foodapp.foodapp.order;

import com.foodapp.foodapp.product.ProductDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
public class OrderDto {
    private Long id;
    private Long companyId;
    private String name;
    private BigDecimal price;
    private String description;
    private OrderType orderType;
    private OrderStatus status;
    private String deliveryAddress;
    private String customerName;
    private LocalDateTime deliveryTime;
    private Set<ProductDto> products;
    private LocalDateTime approvalDeadline;
}
