package com.foodapp.foodapp.order;

import com.foodapp.foodapp.advice.BusinessException;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.order.sql.Order;
import com.foodapp.foodapp.order.sql.OrderRepository;
import com.foodapp.foodapp.orderProduct.OrderProductDto;
import com.foodapp.foodapp.product.ProductDto;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
public class OrderValidator {
    private final ContextProvider contextProvider;
    private final OrderRepository orderRepository;

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

    public void validateOrderSave(final OrderDto order, final Long companyId) {
        validateOrderProducts(order.getOrderProducts(), companyId);
        if(!List.of(OrderStatus.IN_EXECUTION, OrderStatus.EXECUTED).contains(order.getStatus())){
            throw new SecurityException("Wrong order status");
        }
    }

    private void validateOrderProducts(final List<OrderProductDto> orderProducts, final Long companyId) {
        if (CollectionUtils.isEmpty(orderProducts)) {
            throw new IllegalArgumentException("Order has no productOrder");
        }
        orderProducts.forEach(orderProduct -> {
            validateProduct(orderProduct.getProduct(), companyId);
        });
    }

    private void validateProduct(final ProductDto product, final Long companyId) {
        if (product == null) {
            throw new IllegalArgumentException("Order has no product");
        }
        if (!product.getCompanyId().equals(companyId)) {
            throw new SecurityException("Wrong company id in product");
        }
        var category = product.getProductCategory();
        if (category != null && !category.getCompanyId().equals(companyId)) {
            throw new SecurityException("Wrong company id in category");
        }
    }

    public Order validateOrderModifyAndReturn(final OrderDto order, final Long companyId, final Long modifiedOrderId) {
        validateOrderSave(order, companyId);
        var modifiedOrder = orderRepository.findById(modifiedOrderId)
                .orElseThrow(() -> new SecurityException("Wrong modified order id"));
        if (!modifiedOrder.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Wrong company id in modified order");
        }
        if (!List.of(OrderStatus.IN_EXECUTION).contains(modifiedOrder.getStatus())){
            throw new SecurityException("Wrong modified order status");

        }
        return modifiedOrder;
    }

    public Order validateOrderFinalizationAndReturn(final Long orderId, final Long companyId) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new SecurityException("Wrong order id"));
        if (!order.getCompany().getId().equals(companyId)) {
            throw new SecurityException("Wrong company id");

        }
        return order;
    }
}
