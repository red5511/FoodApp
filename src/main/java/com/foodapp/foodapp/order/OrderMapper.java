package com.foodapp.foodapp.order;

import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductDto;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderMapper {
    public static List<OrderDto> mapToOrderDtoList(final List<Order> orders) {
        return orders.stream()
                .map(OrderMapper::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public static OrderDto mapToOrderDto(final Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .name("Zamówienie#" + order.getId())
                .companyId(order.getCompany().getId())
                .description(order.getDescription())
                .price(order.getPrice())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .customerName(order.getCustomerName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryTime(order.getDeliveryTime())
                .products(mapToProductsDto(order.getProducts()))
                //.approvalDeadline(order.getId() % 2 == 0 ? order.getApprovalDeadline() : order.getApprovalDeadline().minusSeconds(20))
                .approvalDeadline(order.getApprovalDeadline())
                .build();
    }

    public static Set<ProductDto> mapToProductsDto(final Set<Product> products) {
        return products.stream()
                .map(OrderMapper::mapToProductDto)
                .collect(Collectors.toSet());
    }

    public static ProductDto mapToProductDto(final Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .imgUrl(product.getImgUrl())
                .soldOut(product.isSoldOut())
                .price(product.getPrice())
                .companyId(product.getCompany().getId())
                .build();
    }
}
