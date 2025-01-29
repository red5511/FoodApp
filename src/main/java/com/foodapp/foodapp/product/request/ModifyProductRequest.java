package com.foodapp.foodapp.product.request;

import com.foodapp.foodapp.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModifyProductRequest {
    private Long modifiedId;
    private ProductDto product;
}
