package com.foodapp.foodapp.productCategory;

import com.foodapp.foodapp.product.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@Getter
public class ProductCategoryDto {
    private Long id;
    private Long companyId;
    private String name;
    @ToString.Exclude
    private List<ProductDto> products;
}
