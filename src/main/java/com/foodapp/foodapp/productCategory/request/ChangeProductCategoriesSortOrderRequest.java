package com.foodapp.foodapp.productCategory.request;

import com.foodapp.foodapp.productCategory.ProductCategoryDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeProductCategoriesSortOrderRequest {
    @Schema(required = true)
    private List<ProductCategoryDto> categories;
}
