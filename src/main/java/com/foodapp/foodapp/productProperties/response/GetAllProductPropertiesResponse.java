package com.foodapp.foodapp.productProperties.response;

import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllProductPropertiesResponse {
    private List<ProductPropertiesDto> productPropertiesList;
}
