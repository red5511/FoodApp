package com.foodapp.foodapp.delivery;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
public class DeliveryOptionDto {
    @Setter
    private Long id;
    private Long companyId;
    private BigDecimal deliveryPrice;
    private Float distance;
}
