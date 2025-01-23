package com.foodapp.foodapp.product;

import java.math.BigDecimal;
import java.util.List;

import com.foodapp.foodapp.productCategory.ProductCategoryDto;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductDto {
    private Long id;
    private Long companyId;
    private String name;
    private BigDecimal price;
    private String imgUrl;
    private String description;
    private boolean soldOut;
    private ProductCategoryDto productCategory;
    private List<ProductPropertiesDto> productPropertiesList;
}
