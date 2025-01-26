package com.foodapp.foodapp.productProperties.productProperty;

import java.util.List;
import java.util.stream.Collectors;

public class ProductPropertyMapper {
    public static ProductPropertyDto toProductPropertyDto(final ProductProperty productProperty) {
        var productPropertiesId =
                productProperty.getProductProperties() != null ? productProperty.getProductProperties().getId() : null;
        return ProductPropertyDto.builder()
                .id(productProperty.getId())
                .name(productProperty.getName())
                .price(productProperty.getPrice())
                .productPropertiesId(productPropertiesId)
                .build();
    }

    public static List<ProductPropertyDto> toProductPropertyDto(final List<ProductProperty> productPropertyList) {
        return productPropertyList.stream()
                .map(ProductPropertyMapper::toProductPropertyDto)
                .collect(Collectors.toList());
    }


    public static List<ProductProperty> toProductProperty(final List<ProductPropertyDto> productPropertyDtos) {
        return productPropertyDtos.stream()
                .map(ProductPropertyMapper::toProductProperty)
                .collect(Collectors.toList());
    }

    public static ProductProperty toProductProperty(final ProductPropertyDto productPropertyDto) {
        return ProductProperty.builder()
                .name(productPropertyDto.getName())
                .price(productPropertyDto.getPrice())
                .build();
    }
}
