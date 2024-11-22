package com.foodapp.foodapp.order.response;

import com.foodapp.foodapp.order.OrderStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusModel {
    @Schema(required = true)
    OrderStatus orderStatus;
    @Schema(required = true)
    String translatedValue;
}
