package com.foodapp.foodapp.order.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.foodapp.foodapp.order.OrderActions;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.OrderType;
import com.foodapp.foodapp.order.PaymentMethod;
import com.foodapp.foodapp.orderProduct.OrderProductDto;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(toBuilder = true)
public class OrderDto {
    private Long id;
    private Long companyId;
    private String deliveryCode;
    private String companyName;
    private BigDecimal price;
    @Size(max = 510)
    private String description;
    private OrderType orderType;
    private OrderStatus status;
    private String deliveryAddress;
    private String customerName;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime executionTime;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdDate;
    private List<OrderProductDto> orderProducts;
    private LocalDateTime approvalDeadline;
    private OrderActions actions;
    private PaymentMethod paymentMethod;
    private boolean takeaway;
    private boolean isPaidWhenOrdered;
}
