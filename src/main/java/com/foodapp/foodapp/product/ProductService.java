package com.foodapp.foodapp.product;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.product.request.CreateProductRequest;
import com.foodapp.foodapp.product.request.DeleteProductRequest;
import com.foodapp.foodapp.product.request.GetProductsRequest;
import com.foodapp.foodapp.product.request.ModifyProductRequest;
import com.foodapp.foodapp.security.ContextProvider;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ContextProvider contextProvider;
    private final ProductValidator productValidator;
    private final ProductMapper productMapper;

    public void saveProduct(final CreateProductRequest request) {
        productValidator.validate(request.getProduct(), request.getProduct().getCompanyId());
        var product = productMapper.mapToProductDto(request.getProduct(), request.getProduct().getCompanyId());
        productRepository.save(product);
    }

    public void modifyProduct(final ModifyProductRequest request) {
        productValidator.validate(request.getProduct(), request.getProduct().getCompanyId());
        var product = productMapper.mapToProductDto(request.getProduct(), request.getProduct().getCompanyId());
        var modifiedProduct = productRepository.findById(request.getModifiedId())
                                               .orElseThrow(() -> new SecurityException("Wrong modified product id"));
        if(!modifiedProduct.getCompany().getId().equals(request.getProduct().getCompanyId())) {
            throw new SecurityException("Miss match between comapnyId and modifiedProduct.companyId");
        }
        modifiedProduct.setStatus(ProductStatus.MODIFIED);
        productRepository.save(product);
        productRepository.save(modifiedProduct);
    }

    public void softDeleteProduct(final DeleteProductRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getCompanyId()));
        Product product = productRepository.findById(request.getProductId())
                                           .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setStatus(ProductStatus.DELETED);
        productRepository.save(product);
    }

    public ProductsPagedResult getPagedProducts(final GetProductsRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getCompanyId()));
        var searchParams = CommonMapper.mapToSearchParams(request);
        return productRepository.searchProducts(searchParams);
    }

    public Map<String, List<ProductDto>> getProductsByCategories(final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var productDtoList = productRepository.findByCompanyIdAndStatus(companyId, ProductStatus.ACTIVE).stream()
                                              .map(ProductMapper::mapToProductDto)
                                              .toList();
        return productDtoList.stream().collect(
            Collectors.groupingBy(el -> el.getProductCategory() != null ? el.getProductCategory().getName() : "Bez kategorii", toList()));
    }
}
