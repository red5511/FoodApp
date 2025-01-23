package com.foodapp.foodapp.productProperties;

import java.util.List;
import java.util.stream.Collectors;

import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyMapper;

public class ProductPropertiesMapper {
    public static ProductPropertiesDto toProductPropertiesDto(final ProductProperties productProperties) {
        return ProductPropertiesDto.builder()
                                   .id(productProperties.getId())
                                   .name(productProperties.getName())
                                   .companyId(productProperties.getCompany().getId())
                                   .propertyList(ProductPropertyMapper.toProductPropertyDto(productProperties.getProductPropertyList()))
                                   .required(productProperties.isRequired())
                                   .productIds(productProperties.getProducts().stream().map(Product::getId).collect(Collectors.toList()))
                                   .build();
    }

    public static List<ProductPropertiesDto> toProductPropertiesDto(final List<ProductProperties> productPropertiesList) {
        return productPropertiesList.stream()
                                    .map(ProductPropertiesMapper::toProductPropertiesDto)
                                    .collect(Collectors.toList());
    }


}
