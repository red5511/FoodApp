package com.foodapp.foodapp.productProperties.productProperty;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Getter
@Data
public class ProductPropertyDto implements Serializable {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long productPropertiesId;

}
