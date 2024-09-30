package com.foodapp.foodapp.order;

import com.foodapp.foodapp.order.request.ApproveNewIncomingOrderRequest;
import com.foodapp.foodapp.order.request.CreateOrderRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/save")
    public ResponseEntity<String> saveOrder(final @RequestBody CreateOrderRequest request) {
        orderService.saveOrder(request);
        return ResponseEntity.ok("Saved");
    }

    @PostMapping("/approve")
    public ResponseEntity<String> approveNewIncomingOrder(final @RequestBody ApproveNewIncomingOrderRequest request) {
        orderService.approveNewIncomingOrder(request);
        return ResponseEntity.ok("Approved");
    }
}
