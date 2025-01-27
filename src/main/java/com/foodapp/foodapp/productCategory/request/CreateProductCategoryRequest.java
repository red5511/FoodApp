package com.foodapp.foodapp.productCategory.request;

import com.foodapp.foodapp.productCategory.ProductCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductCategoryRequest {
    private ProductCategoryDto productCategory;
}
