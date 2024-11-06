package com.foodapp.foodapp.order;

import com.foodapp.foodapp.product.Product;
import com.foodapp.foodapp.product.ProductDto;
import com.foodapp.foodapp.product.ProductRepository;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class OrderMapper {
    private final ProductRepository productRepository;

    public List<OrderDto> mapToOrderDtoList(final List<Order> orders) {
        return orders.stream()
                .map(this::mapToOrderDto)
                .collect(Collectors.toList());
    }

    public OrderDto mapToOrderDto(final Order order) {
        var quantityProductMap = order.getContent().getQuantityProductIdMap();
        var products = productRepository.findAllById(quantityProductMap.keySet());
        return OrderDto.builder()
                .id(order.getId())
                .companyId(order.getCompany().getId())
                .description(order.getDescription())
                .price(order.getPrice())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .customerName(order.getCustomerName())
                .deliveryAddress(order.getDeliveryAddress())
                .deliveryTime(order.getDeliveryTime())
                .quantityProductsMap(quantityProductMap)
                .products(mapToProductsDto(products))
                //.approvalDeadline(order.getId() % 2 == 0 ? order.getApprovalDeadline() : order.getApprovalDeadline().minusSeconds(20))
                .approvalDeadline(order.getApprovalDeadline())
                .build();
    }

    public Set<ProductDto> mapToProductsDto(final List<Product> products) {
        return products.stream()
                .map(this::mapToProductDto)
                .collect(Collectors.toSet());
    }

    public ProductDto mapToProductDto(final Product product) {
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
