package com.foodapp.foodapp.product;

import com.foodapp.foodapp.productCategory.ProductCategoryDto;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class ProductDto {
    private Long id;
    private Long companyId;
    private String name;
    private BigDecimal price;
    private BigDecimal deliveryPrice;
    private BigDecimal takeawayPrice;
    private String imgUrl;
    private String description;
    private ProductStatus productStatus;
    private boolean soldOut;
    private ProductCategoryDto productCategory;
    private List<ProductPropertiesDto> productPropertiesList;
}
