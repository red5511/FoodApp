package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.product.ProductMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductCategoryMapper {
    public static ProductCategoryDto toProductCategoryDto(final ProductCategory productCategory) {
        return ProductCategoryDto.builder()
                .id(productCategory.getId())
                .name(productCategory.getName())
                .companyId(productCategory.getCompany().getId())
                .products(ProductMapper.mapToProductsDto(productCategory.getProducts()))
                .build();
    }

    public static List<ProductCategoryDto> toProductCategoryDto(final List<ProductCategory> productCategories) {
        return productCategories.stream()
                .map(ProductCategoryMapper::toProductCategoryDto)
                .collect(Collectors.toList());
    }


    public static ProductCategory toProductCategory(final ProductCategoryDto productCategoryDto,
                                                    final Company company) {
        return ProductCategory.builder()
                .name(productCategoryDto.getName())
                .company(company)
                .build();
    }

    public static List<ProductCategory> toProductCategory(final List<ProductCategoryDto> productCategoryDtos,
                                                          final Company company) {
        return productCategoryDtos.stream()
                .map(el -> toProductCategory(el, company))
                .collect(Collectors.toList());
    }
}
