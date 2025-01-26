package com.foodapp.foodapp.productProperties.request;

import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductPropertiesRequest {
    private ProductPropertiesDto productProperties;
}
