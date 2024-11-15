package com.foodapp.foodapp.order;

import java.util.List;
import java.util.stream.Collectors;

import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.orderProduct.OrderProductMapper;
import com.foodapp.foodapp.product.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class OrderMapper {
    private final ProductRepository productRepository;

    public List<OrderDto> mapToOrderDtoList(final List<Order> orders) {
        return orders.stream()
                     .map(this::mapToOrderDto)
                     .collect(Collectors.toList());
    }

    public OrderDto mapToOrderDto(final Order order) {
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
