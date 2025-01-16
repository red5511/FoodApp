package com.foodapp.foodapp.order;

import com.foodapp.foodapp.order.request.*;
import com.foodapp.foodapp.order.response.GetOrdersConfigResponse;
import com.foodapp.foodapp.order.response.PagedOrdersResponse;
import com.foodapp.foodapp.statistic.StatisticsMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('VIEW_ONLINE_ORDERING')")
    public ResponseEntity<Void> approveNewIncomingOrder(final @RequestBody ApproveNewIncomingOrderRequest request) {
        orderService.approveNewIncomingOrder(request);
        return ResponseEntity.ok().build(); // Return 200 OK with no body
    }

    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('VIEW_ONLINE_ORDERING')")
    public ResponseEntity<Void> rejectNewIncomingOrder(final @RequestBody RejectNewIncomingOrderRequest request) {
        orderService.rejectNewIncomingOrder(request);
        return ResponseEntity.ok().build(); // Return 200 OK with no body
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('VIEW_ORDERS_HISTORY')")
    public ResponseEntity<PagedOrdersResponse> getOrdersForCompany(final @RequestBody GetOrdersForCompanyRequest request) {
        var pagedResult = orderService.getOrders(request);
        var response = PagedOrdersResponse.builder()
                .pagedResult(pagedResult)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/config")
    @PreAuthorize("hasAuthority('VIEW_ONLINE_ORDERING') or hasAuthority('VIEW_ORDERS_HISTORY')")
    public ResponseEntity<GetOrdersConfigResponse> getOrdersConfig(@RequestBody final GetOrdersConfigRequest request) {
        return ResponseEntity.ok(GetOrdersConfigResponse.builder()
                .orderStatusModels(OrderMapper.getStatusModels())
                .statusSeverityMap(OrderMapper.SEVERITY_TO_ORDER_STATUS_MAP)
                .dataRangeModels(StatisticsMapper.getDataRangeModels())
                .build());
    }
}
