package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyMapper;

import java.util.List;
import java.util.stream.Collectors;

public class ProductPropertiesMapper {
    public static ProductPropertiesDto toProductPropertiesDto(final ProductProperties productProperties) {
        List<Long> productIds = productProperties.getProducts() != null ?
                productProperties.getProducts().stream().map(Product::getId).toList() : List.of();
        return ProductPropertiesDto.builder()
                .id(productProperties.getId())
                .name(productProperties.getName())
                .companyId(productProperties.getCompany().getId())
                .propertyList(ProductPropertyMapper.toProductPropertyDto(productProperties.getProductPropertyList()))
                .required(productProperties.isRequired())
                .productIds(productIds)
                .build();
    }

    public static List<ProductPropertiesDto> toProductPropertiesDto(final List<ProductProperties> productPropertiesList) {
        return productPropertiesList.stream()
                .map(ProductPropertiesMapper::toProductPropertiesDto)
                .collect(Collectors.toList());
    }


    public static ProductProperties toProductProperties(final ProductPropertiesDto productPropertiesDto,
                                                        final Company company) {
        return ProductProperties.builder()
                .name(productPropertiesDto.getName())
                .required(productPropertiesDto.isRequired())
                .company(company)
                .productPropertyList(ProductPropertyMapper.toProductProperty(productPropertiesDto.getPropertyList()))
                .build();
    }
}
