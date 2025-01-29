package com.foodapp.foodapp.product;

import com.foodapp.foodapp.common.SearchParams;
import com.foodapp.foodapp.user.Role;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ProductSearchParams extends SearchParams {
    private String global;
    private Long companyId;
    private ProductStatus status;
}
