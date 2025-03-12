package com.foodapp.foodapp.product;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ProductStatusConverter implements AttributeConverter<ProductStatus, String> {

    @Override
    public String convertToDatabaseColumn(ProductStatus status) {
        return status != null ? status.name() : null;
    }

    @Override
    public ProductStatus convertToEntityAttribute(String status) {
        return status != null ? ProductStatus.valueOf(status) : null;
    }
}