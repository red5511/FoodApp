package com.foodapp.foodapp.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.OrderType;
import com.foodapp.foodapp.orderProduct.OrderProductDto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class OrderDto {
    private Long id;
    private Long companyId;
    private BigDecimal price;
    private String description;
    private OrderType orderType;
    private OrderStatus status;
    private String deliveryAddress;
    private String customerName;
    private LocalDateTime deliveryTime;
    private Set<OrderProductDto> orderProducts;
    private LocalDateTime approvalDeadline;
}