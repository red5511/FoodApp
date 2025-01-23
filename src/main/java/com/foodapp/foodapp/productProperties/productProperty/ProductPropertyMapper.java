package com.foodapp.foodapp.productProperties.productProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ProductPropertyMapper {
    public static ProductPropertyDto toProductPropertyDto(final ProductProperty productProperty) {
        return ProductPropertyDto.builder()
                                 .id(productProperty.getId())
                                 .name(productProperty.getName())
                                 .price(productProperty.getPrice())
                                 .productPropertiesId(productProperty.getProductProperties().getId())
                                 .build();
    }

    public static List<ProductPropertyDto> toProductPropertyDto(final List<ProductProperty> productPropertyList) {
        return productPropertyList.stream()
                                  .map(ProductPropertyMapper::toProductPropertyDto)
                                  .collect(Collectors.toList());
    }


}
