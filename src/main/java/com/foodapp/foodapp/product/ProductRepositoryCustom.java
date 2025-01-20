package com.foodapp.foodapp.product;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom {

    ProductsPagedResult searchProducts(ProductSearchParams params);
}
