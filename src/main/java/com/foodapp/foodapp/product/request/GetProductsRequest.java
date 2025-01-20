package com.foodapp.foodapp.product.request;

import com.foodapp.foodapp.common.BasePagedRequest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProductsRequest extends BasePagedRequest {
    private String globalSearch;
    private Long companyId;
}
