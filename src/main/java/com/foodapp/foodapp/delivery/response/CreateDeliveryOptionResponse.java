package com.foodapp.foodapp.delivery.response;

import com.foodapp.foodapp.delivery.DeliveryOptionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateDeliveryOptionResponse {
    private DeliveryOptionDto deliveryOption;
}
