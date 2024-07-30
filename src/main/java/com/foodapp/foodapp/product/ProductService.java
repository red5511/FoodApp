package com.foodapp.foodapp.product;

import com.foodapp.foodapp.product.request.DeleteProductRequest;
import com.foodapp.foodapp.product.request.ModifyProductRequest;
import com.foodapp.foodapp.product.request.SaveProductRequest;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void saveProduct(final SaveProductRequest request) {
        var product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .createdOn(LocalDateTime.now())
                .build();
        productRepository.save(product);
    }

    public void modifyProduct(final ModifyProductRequest request) {
        var product = Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .modifiedOn(LocalDateTime.now())
                .build();
        productRepository.save(product);
    }

    public void deleteProduct(final DeleteProductRequest request) {
        productRepository.deleteById(request.getProductId());
    }
}
