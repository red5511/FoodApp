package com.foodapp.foodapp.productCategory;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductCategoryDto {
    private Long id;
    private Long companyId;
    private String name;
}
