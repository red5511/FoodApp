package com.foodapp.foodapp.order.sql;

import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.order.PaymentMethod;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class PaymentMethodConverter implements AttributeConverter<PaymentMethod, String> {

    @Override
    public String convertToDatabaseColumn(PaymentMethod paymentMethod) {
        return paymentMethod != null ? paymentMethod.name() : null;
    }

    @Override
    public PaymentMethod convertToEntityAttribute(String paymentMethod) {
        return paymentMethod != null ? PaymentMethod.valueOf(paymentMethod) : null;
    }
}