package com.foodapp.foodapp.productProperties.response;

import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductPropertiesResponse {
    private ProductPropertiesDto productProperties;
}
