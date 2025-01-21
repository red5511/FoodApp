package com.foodapp.foodapp.product;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ProductDto {
    private Long id;
    private Long companyId;
    private String name;
    private BigDecimal price;
    private String imgUrl;
    private String description;
    private boolean soldOut;
}
