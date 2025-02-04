package com.foodapp.foodapp.productCategory.response;

import com.foodapp.foodapp.productCategory.ProductCategoryDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCategoriesResponse {
    private List<ProductCategoryDto> categories;
}
