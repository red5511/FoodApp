package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.productCategory.request.CreateProductCategoryRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ContextProvider contextProvider;
    private final CompanyRepository companyRepository;


    public List<ProductCategoryDto> getAllProductCategoriesByCompanyId(final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var productCategories = productCategoryRepository.findAllByCompanyId(companyId);
        return ProductCategoryMapper.toProductCategoryDto(productCategories);
    }

    public ProductCategoryDto saveProductCategory(final CreateProductCategoryRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getProductCategory().getCompanyId()));
        var company = companyRepository.findById(request.getProductCategory().getCompanyId())
                .orElseThrow(() -> new SecurityException("Wrong company Id"));
        var productCategory =
                productCategoryRepository.save(ProductCategoryMapper.toProductCategory(request.getProductCategory(), company));
        return ProductCategoryMapper.toProductCategoryDto(productCategory);
    }
}
