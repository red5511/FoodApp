package com.foodapp.foodapp.orderProduct;

import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.product.ProductMapper;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;

import java.util.List;
import java.util.stream.Collectors;

public class OrderProductMapper {
    public static List<OrderProductDto> toOrderProductsDto(final List<OrderProduct> orderProducts, final OrderDto orderDto) {
        return orderProducts.stream()
                .map(orderProduct -> OrderProductMapper.toOrderProductDto(orderProduct, orderDto))
                .collect(Collectors.toList());
    }

    public static OrderProductDto toOrderProductDto(final OrderProduct orderProduct, final OrderDto orderDto) {
        var productPropertiesList = orderProduct.getContent() != null ? orderProduct.getContent().getProductPropertiesList() : null;
        return OrderProductDto.builder()
                .id(orderProduct.getId())
                //.order(orderDto) todo zastanowic sie czy to jest napewno okej bo bedzie nieskonczona rekursja
                .price(orderProduct.getPrice())
                .takeawayPrice(orderProduct.getTakeawayPrice())
                .extraDeliveryPrice(orderProduct.getExtraDeliveryPrice())
                .product(ProductMapper.mapToProductDto(orderProduct.getProduct()))
                .quantity(orderProduct.getQuantity())
                .productPropertiesList(productPropertiesList)
                .build();
    }

    public static List<OrderProduct> toOrderProducts(final List<OrderProductDto> orderProducts, final Company company) {
        return orderProducts.stream()
                .map(el -> OrderProductMapper.toOrderProduct(el, company))
                .collect(Collectors.toList());
    }

    private static OrderProduct toOrderProduct(final OrderProductDto orderProductDto, final Company company) {
        return OrderProduct.builder()
                .quantity(orderProductDto.getQuantity())
                .price(orderProductDto.getPrice())
                .takeawayPrice(orderProductDto.getTakeawayPrice())
                .extraDeliveryPrice(orderProductDto.getExtraDeliveryPrice())
                .content(OrderProductMapper.toOrderProductContent(orderProductDto.getProductPropertiesList()))
                .product(ProductMapper.mapToProduct(orderProductDto.getProduct(), company))
                .note(orderProductDto.getNote())
                .build();
    }

    private static OrderProductContent toOrderProductContent(final List<ProductPropertiesDto> productPropertiesList) {
        return OrderProductContent.builder()
                .productPropertiesList(productPropertiesList)
                .build();
    }
}
