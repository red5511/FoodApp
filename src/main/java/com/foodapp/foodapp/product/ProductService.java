package com.foodapp.foodapp.product;

import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.product.request.CreateProductRequest;
import com.foodapp.foodapp.product.request.GetProductsRequest;
import com.foodapp.foodapp.product.request.ModifyProductRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.foodapp.foodapp.product.ProductMapper.NO_CATEGORY;
import static java.util.stream.Collectors.toList;

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
        if (!modifiedProduct.getCompany().getId().equals(request.getProduct().getCompanyId())) {
            throw new SecurityException("Miss match between comapnyId and modifiedProduct.companyId");
        }
        modifiedProduct.setStatus(ProductStatus.MODIFIED);
        productRepository.save(product);
        productRepository.save(modifiedProduct);
    }

    @Transactional
    public void softDeleteProducts(final List<Long> productIds, final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var products = productRepository.findAllById(productIds);
        products.forEach(product -> {
            if (!product.getCompany().getId().equals(companyId)) {
                throw new SecurityException("Missmatch in company id vs product id");
            }
            product.setStatus(ProductStatus.DELETED);
        });
        productRepository.saveAll(products);
    }

    public ProductsPagedResult getPagedProducts(final GetProductsRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getCompanyId()));
        var searchParams = CommonMapper.mapToSearchParams(request);
        return productRepository.searchProducts(searchParams);
    }

    public Map<String, List<ProductDto>> getProductsByCategories(final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
        var productDtoList = productRepository.findByCompanyIdAndStatus(companyId, ProductStatus.ACTIVE).stream()
                .sorted(
                        Comparator.comparing(Product::getName)
                ).map(ProductMapper::mapToProductDto)
                .toList();
        return productDtoList.stream().collect(
                Collectors.groupingBy(el -> el.getProductCategory() != null ? el.getProductCategory().getName() : NO_CATEGORY,
                        toList()));
    }

}
