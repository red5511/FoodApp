package com.foodapp.foodapp.productProperties;

import java.util.List;

import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductPropertiesDto {
    private Long id;
    private List<Long> productIds;
    private Long companyId;
    private String name;
    private List<ProductPropertyDto> propertyList;
    private boolean required;
}
