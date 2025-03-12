package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.productProperties.productProperty.ProductProperty;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyDto;
import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyMapper;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
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
                .maxChosenOptions(productProperties.getMaxChosenOptions())
                .build();
    }

    public static List<ProductPropertiesDto> toProductPropertiesDto(final List<ProductProperties> productPropertiesList) {
        return productPropertiesList.stream()
                .sorted(
                        Comparator.comparing(ProductProperties::isRequired).reversed()
                                .thenComparing(ProductProperties::getCreatedDate)
                )
                .map(ProductPropertiesMapper::toProductPropertiesDto)
                .collect(Collectors.toList());
    }


    public static List<ProductProperties> toProductProperties(final List<ProductPropertiesDto> productPropertiesDto,
                                                              final Company company) {
        return productPropertiesDto.stream()
                .map(el -> ProductPropertiesMapper.toProductProperties(el, company))
                .collect(Collectors.toList());
    }

    public static ProductProperties toProductProperties(final ProductPropertiesDto productPropertiesDto,
                                                        final Company company) {
        return ProductProperties.builder()
                .name(productPropertiesDto.getName())
                .required(productPropertiesDto.isRequired())
                .company(company)
                .productPropertyList(ProductPropertyMapper.toProductProperty(productPropertiesDto.getPropertyList()))
                .maxChosenOptions(productPropertiesDto.getMaxChosenOptions())
                .build();
    }

    public static void modifiedProductProperties(final ProductPropertiesDto productPropertiesDto,
                                                 final ProductProperties productProperties) {
        productProperties.setName(productPropertiesDto.getName());
        productProperties.setRequired(productPropertiesDto.isRequired());
        productProperties.setMaxChosenOptions(productPropertiesDto.getMaxChosenOptions());

        var propertyDtoListMap = productPropertiesDto.getPropertyList().stream()
                .collect(Collectors.toMap(ProductPropertyDto::getId, Function.identity()));
        var legitModifiedPropertyList = productProperties.getProductPropertyList().stream()
                .filter(el -> propertyDtoListMap.containsKey(el.getId()))
                .peek(el -> {
                    var modifiedPropertyDto = propertyDtoListMap.get(el.getId());
                    el.setName(modifiedPropertyDto.getName());
                    el.setPrice(modifiedPropertyDto.getPrice());
                })
                .collect(Collectors.toList());
        List<ProductProperty> currentProperties = productProperties.getProductPropertyList();
        currentProperties.clear();
        currentProperties.addAll(legitModifiedPropertyList);
    }
}
