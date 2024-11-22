package com.foodapp.foodapp.order;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.order.response.OrderStatusModel;
import com.foodapp.foodapp.orderProduct.OrderProductMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderMapper {
    public static final Map<OrderStatus, String> TRANSLATED_ORDER_STATUSES_MAP = Map.of(
        OrderStatus.WAITING_FOR_ACCEPTANCE, "W akceptacji",
        OrderStatus.IN_EXECUTION, "W realizacji",
        OrderStatus.EXECUTED, "Wykonane",
        OrderStatus.REJECTED, "Odrzucone"
    );
    public static final Map<OrderStatus, Severity> SEVERITY_TO_ORDER_STATUS_MAP = Map.of(
        OrderStatus.WAITING_FOR_ACCEPTANCE, Severity.info,
        OrderStatus.IN_EXECUTION, Severity.warning,
        OrderStatus.EXECUTED, Severity.success,
        OrderStatus.REJECTED, Severity.danger
    );

    public static List<OrderStatusModel> getStatusModels() {
        var statuses = OrderStatus.values();
        return Arrays.stream(statuses)
                     .map(status -> OrderStatusModel.builder()
                                                    .orderStatus(status)
                                                    .translatedValue(TRANSLATED_ORDER_STATUSES_MAP.get(status))
                                                    .build())
                     .collect(Collectors.toList());
    }

    public static List<OrderDto> mapToOrderDtoList(final List<Order> orders) {
        return orders.stream()
                     .map(OrderMapper::mapToOrderDto)
                     .collect(Collectors.toList());
    }

    public static OrderDto mapToOrderDto(final Order order) {
        var orderProducts = order.getOrderProducts();

        var orderDto = OrderDto.builder()
                               .id(order.getId())
                               .companyId(order.getCompany().getId())
                               .description(order.getDescription())
                               .price(order.getPrice())
                               .orderType(order.getOrderType())
                               .status(order.getStatus())
                               .customerName(order.getCustomerName())
                               .deliveryAddress(order.getDeliveryAddress())
                               .deliveryTime(order.getDeliveryTime())
                               //.approvalDeadline(order.getId() % 2 == 0 ? order.getApprovalDeadline() : order.getApprovalDeadline().minusSeconds(20))
                               .approvalDeadline(order.getApprovalDeadline())
                               .build();
        orderDto = orderDto.toBuilder()
                           .orderProducts(OrderProductMapper.toOrderProductsDto(orderProducts, orderDto))
                           .build();
        return orderDto;
    }

}
