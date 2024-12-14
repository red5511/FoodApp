package com.foodapp.foodapp.product;

import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.product.request.CreateProductRequest;
import com.foodapp.foodapp.product.request.DeleteProductRequest;
import com.foodapp.foodapp.product.request.ModifyProductRequest;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;
    private final ContextProvider contextProvider;

    public void saveProduct(final CreateProductRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getProduct().getCompanyId()));
        var product = buildProduct(request.getProduct());
        productRepository.save(product);
    }

    public void modifyProduct(final ModifyProductRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getProduct().getCompanyId()));
        var product = buildProduct(request.getProduct());
        productRepository.save(product);
    }

    public void deleteProduct(final DeleteProductRequest request) {
        contextProvider.validateCompanyAccess(List.of(request.getCompanyId()));
        productRepository.deleteById(request.getProductId());
    }

    private Product buildProduct(final ProductDto productDto) {
        var company = companyRepository.findById(productDto.getCompanyId())
                .orElseThrow(() -> new SecurityException("Company id not valid"));
        return Product.builder()
                .name(productDto.getName())
                .price(productDto.getPrice())
                .description(productDto.getDescription())
                .imgUrl(productDto.getImgUrl())
                .company(company)
                .build();
    }
}
