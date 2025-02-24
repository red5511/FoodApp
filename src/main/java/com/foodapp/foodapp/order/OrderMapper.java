package com.foodapp.foodapp.order;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.bluetooth.BluetoothPrinter;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.order.response.CreateOrderRequestResponse;
import com.foodapp.foodapp.order.response.OrderStatusModel;
import com.foodapp.foodapp.orderProduct.OrderProduct;
import com.foodapp.foodapp.orderProduct.OrderProductMapper;
import lombok.AllArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
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
                .createdDate(order.getCreatedDate())
                .paymentMethod(order.getPaymentMethod())
                .isPaidWhenOrdered(order.isPaidWhenOrdered())
                .build();
        orderDto = orderDto.toBuilder()
                .orderProducts(OrderProductMapper.toOrderProductsDto(orderProducts, orderDto))
                .build();
        return orderDto;
    }

    public static Order mapToOrder(final OrderDto orderDto, final Company company, final Long parentId) {
        var orderProducts = OrderProductMapper.toOrderProducts(orderDto.getOrderProducts(), company);
        var order = Order.builder()
                .parentId(parentId)
                .orderType(orderDto.getOrderType())
                .company(company)
                .description(orderDto.getDescription())
                .price(orderDto.getPrice())
                .status(orderDto.getStatus() == null ? OrderStatus.WAITING_FOR_ACCEPTANCE : orderDto.getStatus())
                .customerName(orderDto.getCustomerName())
                .deliveryAddress(orderDto.getDeliveryAddress())
                .executionTime(orderDto.getExecutionTime() == null ? LocalDateTime.now() : orderDto.getExecutionTime())
                .deliveryCode(orderDto.getDeliveryCode())
                .paymentMethod(orderDto.getPaymentMethod())
                .approvalDeadline(orderDto.getApprovalDeadline())
                .orderProducts(orderProducts)
                .takeaway(orderDto.isTakeaway())
                .isPaidWhenOrdered(orderDto.isPaidWhenOrdered())
                .orderType(OrderType.OWN)
                .build();
        for (OrderProduct orderProduct : orderProducts) {
            orderProduct.setOrder(order);
        }
        return order;
    }

    public static OrderActions getActions(final Order order) {
        return OrderActions.builder()
                .showApprove(showApprove(order))
                .showReject(showReject(order))
                .showSetExecutionTime(showSetExecutionTime(order))
                .showPrint(showPrint(order))
                .showToTheCashier(showToTheCashier(order))
                .showCancel(showCancel(order))
                .showReadyToPickUp(showReadyToPickUp(order))
                .showModify(showModify(order))
                .build();
    }

    private static boolean showPrint(final Order order) {
        return true;
    }

    private static boolean showModify(final Order order) {
        return order.getStatus() == OrderStatus.IN_EXECUTION &&
                OrderType.OWN.equals(order.getOrderType());
    }

    private static boolean showReadyToPickUp(final Order order) {
        return order.getStatus() == OrderStatus.IN_EXECUTION && !OrderType.OWN.equals(order.getOrderType());
    }

    private static boolean showCancel(final Order order) {
        return order.getStatus() == OrderStatus.IN_EXECUTION;
    }

    private static boolean showToTheCashier(final Order order) {
        return order.getStatus() == OrderStatus.IN_EXECUTION && !order.isPaidWhenOrdered();
    }

    public static boolean showApprove(final Order order) {
        return order.getStatus() == OrderStatus.WAITING_FOR_ACCEPTANCE;
    }

    public static boolean showReject(final Order order) {
        return order.getStatus() == OrderStatus.WAITING_FOR_ACCEPTANCE;
    }

    public static boolean showSetExecutionTime(final Order order) {
        return order.getStatus() == OrderStatus.WAITING_FOR_ACCEPTANCE;
    }

    public static CreateOrderRequestResponse createOrderResponse(final OrderDto order,
                                                                 final Long orderId,
                                                                 final boolean printViaBluetooth)
            throws UnsupportedEncodingException {
        return CreateOrderRequestResponse.builder()
                .orderId(orderId)
                .encodedTextForBluetoothPrinterList(
                        printViaBluetooth ? BluetoothPrinter.encodeTextForBluetooth(order, orderId) : List.of())
                .build();
    }
}
