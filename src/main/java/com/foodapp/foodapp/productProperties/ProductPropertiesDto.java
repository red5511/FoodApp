package com.foodapp.foodapp.productProperties;

import com.foodapp.foodapp.productProperties.productProperty.ProductPropertyDto;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
public class ProductPropertiesDto implements Serializable {
    private Long id;
    private List<Long> productIds;
    private Long companyId;
    private String name;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<ProductPropertyDto> propertyList;
    private boolean required;
}