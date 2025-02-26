package com.foodapp.foodapp.delivery.response;

import com.foodapp.foodapp.delivery.DeliveryOptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllDeliveryOptionsResponse {
    private List<DeliveryOptionDto> deliveryOptions;
}
