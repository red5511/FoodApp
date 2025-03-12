package com.foodapp.foodapp.order.sql;

import com.foodapp.foodapp.order.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus status) {
        return status != null ? status.name() : null;
    }

    @Override
    public OrderStatus convertToEntityAttribute(String status) {
        return status != null ? OrderStatus.valueOf(status) : null;
    }
}