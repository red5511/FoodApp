package com.foodapp.foodapp.product.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetProductsByCategoriesResponse {
    private List<ProductsByCategoryTabView> menuOrderingTabs;
}
