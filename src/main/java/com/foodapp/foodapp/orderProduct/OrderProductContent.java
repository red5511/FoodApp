package com.foodapp.foodapp.orderProduct;


import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OrderProductContent implements Serializable {
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ProductPropertiesDto> productPropertiesList;
}
