package com.foodapp.foodapp.order.request;

import com.foodapp.foodapp.order.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalizeOrderRequest {
    private PaymentMethod paymentMethod;
    private boolean takeaway;
    private BigDecimal newTotalPrice;
}
