package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.productCategory.request.CreateProductCategoryRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ContextProvider contextProvider;
    private final CompanyRepository companyRepository;


    public List<ProductCategoryDto> getAllProductCategoriesByCompanyId(final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        Sort sort = Sort.by(Sort.Order.asc("sortOrder").nullsLast());
        var productCategories = productCategoryRepository.findAllByCompanyId(companyId, sort);
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

    public void changeProductCategoriesSortOrder(final List<ProductCategoryDto> categoriesDto,
                                                 final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var categoryIds = categoriesDto.stream().map(ProductCategoryDto::getId).toList();
        var categories = productCategoryRepository.findAllById(categoryIds);
        validate(categories, companyId);
        for (int i = 0; i < categories.size(); i++) {
            categories.get(i).setSortOrder(i);
        }
        productCategoryRepository.saveAll(categories);
    }

    private void validate(final List<ProductCategory> categories, final Long companyId) {
        var oneEntryCompanyIdSet = categories.stream()
                .map(el -> el.getCompany().getId())
                .collect(Collectors.toSet());
        if (oneEntryCompanyIdSet.size() > 1 || !oneEntryCompanyIdSet.contains(companyId)) {
            throw new SecurityException("Wrong company id in categories");
        }
    }
}
