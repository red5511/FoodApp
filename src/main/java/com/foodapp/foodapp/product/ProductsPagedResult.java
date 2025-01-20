package com.foodapp.foodapp.product;

import java.util.List;

import com.foodapp.foodapp.user.UserDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProductsPagedResult {
    private List<ProductDto> products;
    private long totalRecords;
}
