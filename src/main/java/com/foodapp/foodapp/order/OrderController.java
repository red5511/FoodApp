package com.foodapp.foodapp.order;

import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.order.request.ApproveNewIncomingOrderRequest;
import com.foodapp.foodapp.order.request.CreateOrderRequest;
import com.foodapp.foodapp.order.request.GetOrdersForCompanyRequest;
import com.foodapp.foodapp.order.request.RejectNewIncomingOrderRequest;
import com.foodapp.foodapp.order.response.PagedOrdersResponse;
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
    public ResponseEntity<Void> saveOrder(final @RequestBody CreateOrderRequest request) {
        orderService.saveOrder(request);
        return ResponseEntity.ok().build(); // Return 200 OK with no body
    }

    @PostMapping("/approve")
    public ResponseEntity<Void> approveNewIncomingOrder(final @RequestBody ApproveNewIncomingOrderRequest request) {
        orderService.approveNewIncomingOrder(request);
        return ResponseEntity.ok().build(); // Return 200 OK with no body
    }

    @PostMapping("/reject")
    public ResponseEntity<Void> rejectNewIncomingOrder(final @RequestBody RejectNewIncomingOrderRequest request) {
        orderService.rejectNewIncomingOrder(request);
        return ResponseEntity.ok().build(); // Return 200 OK with no body
    }

    @PostMapping("/orders")
    public ResponseEntity<PagedOrdersResponse> getOrdersForCompany(final @RequestBody GetOrdersForCompanyRequest request) {
        var searchParams =
                CommonMapper.mapToSearchParams(request.getFilters(), request.getSize(), request.getPage(), request.getCompanyId());
        var pagedResult = orderService.getOrders(searchParams);
        var result = PagedOrdersResponse.builder()
                .pagedResult(pagedResult)
                .build();
        return ResponseEntity.ok(result);
    }
}
