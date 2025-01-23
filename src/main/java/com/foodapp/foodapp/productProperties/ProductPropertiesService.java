package com.foodapp.foodapp.productProperties;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductPropertiesService {
    private final ProductPropertiesRepository productPropertiesRepository;

    public List<ProductPropertiesDto> getAllProductPropertiesByCompanyId(final Long companyId) {
        var productProperties = productPropertiesRepository.findAllByCompanyId(companyId);
        return ProductPropertiesMapper.toProductPropertiesDto(productProperties);
    }
}
