package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductService;
import com.foodapp.foodapp.productCategory.request.CreateProductCategoryRequest;
import com.foodapp.foodapp.productCategory.request.DeleteProductCategoryRequest;
import com.foodapp.foodapp.productCategory.request.ModifyProductCategoryRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@AllArgsConstructor
public class ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ContextProvider contextProvider;
    private final CompanyRepository companyRepository;
    private final ProductService productService;


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
        var categoryByIdMap = productCategoryRepository.findAllById(categoryIds).stream()
                .collect(Collectors.toMap(ProductCategory::getId, Function.identity()));
        validate(categoryByIdMap.values(), companyId, categoryIds);
        for (int i = 0; i < categoriesDto.size(); i++) {
            var category = categoryByIdMap.get(categoriesDto.get(i).getId());
            category.setSortOrder(i);
        }
        productCategoryRepository.saveAll(categoryByIdMap.values());
    }

    public void modifyProductCategory(final ModifyProductCategoryRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getCategory().getCompanyId()));
        var modifiedCategory = productCategoryRepository.findById(request.getModifiedId())
                .orElseThrow(() -> new SecurityException("Wrong modified category id"));
        if (!modifiedCategory.getCompany().getId().equals(request.getCategory().getCompanyId())) {
            throw new SecurityException("Miss match between comapnyId and modifiedProductCategory.companyId");
        }
        modifiedCategory.setName(request.getCategory().getName());
        productCategoryRepository.save(modifiedCategory);
    }

    @Transactional
    public void deleteProductCategory(final DeleteProductCategoryRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getCompanyId()));
        var category = productCategoryRepository.findById(request.getProductCategoryId())
                .orElseThrow(() -> new SecurityException("Wrong category id"));
        if (!category.getCompany().getId().equals(request.getCompanyId())) {
            throw new SecurityException("Miss match between comapnyId and ProductCategory.companyId");
        }
        var products = category.getProducts();
        if (!CollectionUtils.isEmpty(products)) {
            List<Product> productsCopy = new ArrayList<>(products);
            for (Product product : productsCopy) {
                product.setProductCategory(null);
                category.getProducts().remove(product);
            }
        }
        productCategoryRepository.delete(category);
    }

    @Transactional
    public void deleteProductCategoryWithProducts(final DeleteProductCategoryRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getCompanyId()));
        var category = productCategoryRepository.findById(request.getProductCategoryId())
                .orElseThrow(() -> new SecurityException("Wrong category id"));
        if (!category.getCompany().getId().equals(request.getCompanyId())) {
            throw new SecurityException("Miss match between comapnyId and ProductCategory.companyId");
        }
        var products = category.getProducts();
        if (!CollectionUtils.isEmpty(products)) {
            List<Product> productsCopy = new ArrayList<>(products);
            for (Product product : productsCopy) {
                product.setProductCategory(null);
                category.getProducts().remove(product);
            }
            var productsIds = productsCopy.stream()
                    .map(Product::getId).toList();
            productService.softDeleteProducts(productsIds, request.getCompanyId());
        }
        productCategoryRepository.delete(category);
    }

    private void validate(final Collection<ProductCategory> categories, final Long companyId, final List<Long> categoryIds) {
        if (categoryIds.size() != categories.size()) {
            throw new SecurityException("Wrong list sizes");
        }
        var oneEntryCompanyIdSet = categories.stream()
                .map(el -> el.getCompany().getId())
                .collect(Collectors.toSet());
        if (oneEntryCompanyIdSet.size() > 1 || !oneEntryCompanyIdSet.contains(companyId)) {
            throw new SecurityException("Wrong company id in categories");
        }
    }
}
