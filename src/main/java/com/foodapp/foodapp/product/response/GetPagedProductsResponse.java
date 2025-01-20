package com.foodapp.foodapp.product.response;

import com.foodapp.foodapp.product.ProductsPagedResult;
import com.foodapp.foodapp.user.UsersPagedResult;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPagedProductsResponse {
    private ProductsPagedResult pagedResult;
}
