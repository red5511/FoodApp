package com.foodapp.foodapp.productProperties.request;

import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModifyProductPropertiesRequest {
    private ProductPropertiesDto productProperties;
}
