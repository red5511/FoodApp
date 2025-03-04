package com.foodapp.foodapp.orderProduct;

import com.foodapp.foodapp.order.dto.OrderDto;
import com.foodapp.foodapp.product.ProductDto;
import com.foodapp.foodapp.productProperties.ProductPropertiesDto;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class OrderProductDto {
    private Long id;
    private OrderDto order;
    private ProductDto product;
    private int quantity;
    private BigDecimal price;
    private BigDecimal extraDeliveryPrice;
    private BigDecimal takeawayPrice;
    private List<ProductPropertiesDto> productPropertiesList;
    @Size(max = 510)
    private String note;
}
