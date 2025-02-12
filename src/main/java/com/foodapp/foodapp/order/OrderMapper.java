package com.foodapp.foodapp.order;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.order.response.OrderStatusModel;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import com.foodapp.foodapp.orderProduct.OrderProductMapper;
import lombok.AllArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OrderMapper {
    public static final Map<OrderStatus, String> TRANSLATED_ORDER_STATUSES_MAP = Map.of(
            OrderStatus.WAITING_FOR_ACCEPTANCE, "W akceptacji",
            OrderStatus.IN_EXECUTION, "W realizacji",
            OrderStatus.EXECUTED, "Wykonane",
            OrderStatus.REJECTED, "Odrzucone",
            OrderStatus.READY_FOR_PICK_UP, "Do odebrania",
            OrderStatus.NOT_ACCEPTED, "Nie podjęte"
    );
    public static final Map<OrderStatus, Severity> SEVERITY_TO_ORDER_STATUS_MAP = Map.of(
            OrderStatus.WAITING_FOR_ACCEPTANCE, Severity.yellow,
            OrderStatus.IN_EXECUTION, Severity.warning,
            OrderStatus.EXECUTED, Severity.success,
            OrderStatus.REJECTED, Severity.danger,
            OrderStatus.READY_FOR_PICK_UP, Severity.info,
            OrderStatus.NOT_ACCEPTED, Severity.contrast
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
                .deliveryCode(order.getDeliveryCode())
                .companyName(order.getCompany().getName())
                .description(order.getDescription())
                .price(order.getPrice())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .customerName(order.getCustomerName())
                .deliveryAddress(order.getDeliveryAddress())
                .takeaway(order.isTakeaway())
                .executionTime(order.getExecutionTime())
                //.approvalDeadline(order.getId() % 2 == 0 ? order.getApprovalDeadline() : order.getApprovalDeadline().minusSeconds(20))
                .approvalDeadline(order.getApprovalDeadline())
                .actions(getActions(order))
                .build();
        orderDto = orderDto.toBuilder()
                .orderProducts(OrderProductMapper.toOrderProductsDto(orderProducts, orderDto))
                .createdDate(order.getCreatedDate())
                .paymentMethod(order.getPaymentMethod())
                .build();
        return orderDto;
    }

    public static Order mapToOrder(final OrderDto orderDto, final Company company) {
        var orderProducts = OrderProductMapper.toOrderProducts(orderDto.getOrderProducts(), company);
        var order = Order.builder()
                .orderType(orderDto.getOrderType())
                .company(company)
                .description(orderDto.getDescription())
                .price(orderDto.getPrice())
                .status(orderDto.getStatus() == null ? OrderStatus.WAITING_FOR_ACCEPTANCE : orderDto.getStatus())
                .customerName(orderDto.getCustomerName())
                .deliveryAddress(orderDto.getDeliveryAddress())
                .executionTime(orderDto.getExecutionTime())
                .deliveryCode(orderDto.getDeliveryCode())
                .paymentMethod(orderDto.getPaymentMethod())
                .approvalDeadline(orderDto.getApprovalDeadline())
                .orderProducts(orderProducts)
                .takeaway(orderDto.isTakeaway())
                .build();
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrder(order);
        }
        return order;
    }

    public static OrderActions getActions(Order order) {
        return OrderActions.builder()
                .showApprove(order.getStatus() == OrderStatus.WAITING_FOR_ACCEPTANCE)
                .showReject(order.getStatus() == OrderStatus.WAITING_FOR_ACCEPTANCE)
//                .showApprove(true)
//                .showReject(true)
                .showSetDeliveryTime(order.getStatus() == OrderStatus.WAITING_FOR_ACCEPTANCE)
                .showPrint(true)
                .showReadyToPickUp(order.getStatus() == OrderStatus.IN_EXECUTION)
                .build();
    }
}
