package com.foodapp.foodapp.product;

import java.util.List;
import java.util.stream.Collectors;

import com.foodapp.foodapp.productCategory.ProductCategoryDto;

public class ProductMapper {
    public static List<ProductDto> mapToProductsDto(final List<Product> products) {
        return products.stream()
                       .map(ProductMapper::mapToProductDto)
                       .collect(Collectors.toList());
    }

    public static ProductDto mapToProductDto(final Product product) {
        return ProductDto.builder()
                         .id(product.getId())
                         .name(product.getName())
                         .description(product.getDescription())
                         .imgUrl(product.getImgUrl())
                         .soldOut(product.isSoldOut())
                         .price(product.getPrice())
                         .companyId(product.getCompany().getId())
                         .productCategory(ProductCategoryDto.builder()
                                                            .companyId(product.getCompany().getId())
                                                            .name(product.getCompany().getName())
                                                            .id(product.getCompany().getId())
                                                            .build())
                         .build();
    }
}
