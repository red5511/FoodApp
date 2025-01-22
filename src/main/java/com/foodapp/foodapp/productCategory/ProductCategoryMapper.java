package com.foodapp.foodapp.productCategory;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryMapper {
    public static ProductCategoryDto toProductCategoryDto(final ProductCategory productCategory) {
        return ProductCategoryDto.builder()
                                 .id(productCategory.getId())
                                 .name(productCategory.getName())
                                 .companyId(productCategory.getCompany().getId())
                                 .build();
    }

    public static List<ProductCategoryDto> toProductCategoryDto(final List<ProductCategory> productCategories) {
        return productCategories.stream()
                                .map(ProductCategoryMapper::toProductCategoryDto)
                                .collect(Collectors.toList());
    }


}
