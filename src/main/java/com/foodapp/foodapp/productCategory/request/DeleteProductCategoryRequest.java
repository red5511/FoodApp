package com.foodapp.foodapp.productCategory.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteProductCategoryRequest {
    private Long productCategoryId;
    private Long companyId;
}
