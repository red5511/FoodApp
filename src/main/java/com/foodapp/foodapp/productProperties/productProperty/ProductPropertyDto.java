package com.foodapp.foodapp.productProperties.productProperty;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
@Data
public class ProductPropertyDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long productPropertiesId;

}
