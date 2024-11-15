package com.foodapp.foodapp.orderProduct;

import java.math.BigDecimal;

import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.product.ProductDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderProductDto {
    private Long id;

    private OrderDto order;

    private ProductDto product;

    private int quantity;

    private BigDecimal price;
}
