package com.foodapp.foodapp.product.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveProductRequest {
    private String name;
    private String price;
    private String imgUrl;
    private String description;
}
