package com.foodapp.foodapp.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ProductDto {
    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    private Long companyId;
    @Schema(required = true)
    private String name;
    @Schema(required = true)
    private BigDecimal price;
    private String imgUrl;
    private String description;
    private boolean soldOut;
}
