package com.foodapp.foodapp.product.response;

import java.util.List;

import com.foodapp.foodapp.product.ProductsPagedResult;
import com.foodapp.foodapp.productCategory.ProductCategoryDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPagedProductsResponse {
    private ProductsPagedResult pagedResult;
    private List<ProductCategoryDto> productCategories;
}
