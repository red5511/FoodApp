package com.foodapp.foodapp.order;

import com.foodapp.foodapp.advice.BusinessException;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDateTime;

@AllArgsConstructor
public class OrderValidator {
    private final long timeToAcceptOrder;

    @SneakyThrows
    public void validateApprovingOrder(final Order order, final Long companyId) {
        if (OrderStatus.WAITING_FOR_ACCEPTANCE != order.getStatus() || !companyId.equals(order.getCompany().getId())) {
            throw new SecurityException("Wrong action");
        }
        if (LocalDateTime.now().isAfter(order.getCreatedDate().plusMinutes(timeToAcceptOrder))) {
            throw new BusinessException("Time for accepting order has passed");
        }
    }
}
