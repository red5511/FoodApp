package com.foodapp.foodapp.order.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequestResponse {
    @Schema(required = true)
    Long orderId;
    @Schema(required = true)
    List<String> encodedTextForBluetoothPrinterList;
}
