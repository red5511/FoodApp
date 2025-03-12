package com.foodapp.foodapp.productCategory.request;

import com.foodapp.foodapp.productCategory.ProductCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModifyProductCategoryRequest {
    private Long modifiedId;
    private ProductCategoryDto category;
}


