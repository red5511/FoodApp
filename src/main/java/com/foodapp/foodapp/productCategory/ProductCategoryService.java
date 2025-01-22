package com.foodapp.foodapp.productCategory;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;

    public List<ProductCategoryDto> getAllProductCategoriesByCompanyId(final Long companyId) {
        var productCategories = productCategoryRepository.findAllByCompanyId(companyId);
        return ProductCategoryMapper.toProductCategoryDto(productCategories);
    }
}
