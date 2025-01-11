package com.foodapp.foodapp.order;

import com.foodapp.foodapp.advice.BusinessException;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class OrderValidator {
    private final ContextProvider contextProvider;

    @SneakyThrows
    public void validateWaitingForAcceptanceOrder(final Order order, final Long companyId) {
        if (OrderStatus.WAITING_FOR_ACCEPTANCE != order.getStatus()) {
            throw new SecurityException("Wrong status");
        }
        if (ContextProvider.HOLDING_ID.equals(companyId)) {
            contextProvider.validateCompanyAccess(List.of(order.getCompany().getId()));
        } else if (!companyId.equals(order.getCompany().getId())) {
            throw new SecurityException("Wrong action");
        }
        if (LocalDateTime.now().isAfter(order.getApprovalDeadline())) {
            throw new BusinessException("Czas na zaakceptowanie zamówienia minął");
        }
    }
}
