package com.foodapp.foodapp.orderProduct;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.product.ProductMapper;

public class OrderProductMapper {
    public static Set<OrderProductDto> toOrderProductsDto(final List<OrderProduct> orderProducts, final OrderDto orderDto) {
        return orderProducts.stream()
                            .map(orderProduct -> OrderProductMapper.toOrderProductDto(orderProduct, orderDto))
                            .collect(Collectors.toSet());
    }

    public static OrderProductDto toOrderProductDto(final OrderProduct orderProduct, final OrderDto orderDto) {
        return OrderProductDto.builder()
                              //.order(orderDto) todo zastanowic sie czy to jest napewno okej bo bedzie nieskonczona rekursja
                              .price(orderProduct.getPrice())
                              .product(ProductMapper.mapToProductDto(orderProduct.getProduct()))
                              .quantity(orderProduct.getQuantity())
                              .build();
    }
}
