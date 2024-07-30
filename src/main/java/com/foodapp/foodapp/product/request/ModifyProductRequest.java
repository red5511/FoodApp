package com.foodapp.foodapp.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModifyProductRequest {
    private String name;
    private String price;
    private String imgUrl;
    private String description;
}
