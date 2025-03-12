package com.foodapp.foodapp.order;

import com.foodapp.foodapp.order.request.*;
import com.foodapp.foodapp.order.response.CreateOrderRequestResponse;
import com.foodapp.foodapp.order.response.GetOrdersConfigResponse;
import com.foodapp.foodapp.order.response.ModifyOrderResponse;
import com.foodapp.foodapp.order.response.PagedOrdersResponse;
import com.foodapp.foodapp.statistic.StatisticsMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/v1/order")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Order")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/save/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING')")
    public ResponseEntity<CreateOrderRequestResponse> saveOrder(final @RequestBody CreateOrderRequest request,
                                                                final @PathVariable Long companyId)
            throws UnsupportedEncodingException {
        var id = orderService.saveOrder(request, companyId);
        return ResponseEntity.ok(id);
    }

    @PostMapping("/modify/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING')")
    public ResponseEntity<ModifyOrderResponse> modifyOrder(final @RequestBody ModifyOrderRequest request,
                                                           final @PathVariable Long companyId) {
        var response = orderService.modifyOrder(request, companyId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{companyId}/{orderId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING')")
    public ResponseEntity<Void> softDeleteOrder(final @PathVariable Long orderId,
                                                final @PathVariable Long companyId) {
        orderService.softDeleteOrder(orderId, companyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/revoke/{companyId}/{orderId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING')")
    public ResponseEntity<Void> revokeToHandleOrder(final @PathVariable Long orderId,
                                                final @PathVariable Long companyId) {
        orderService.revokeToHandleOrder(orderId, companyId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/finalize/{companyId}/{orderId}")
    @PreAuthorize("hasAuthority('VIEW_RESTAURANT_ORDERING')")
    public ResponseEntity<Void> finalizeOrder(final @RequestBody FinalizeOrderRequest request, final @PathVariable Long companyId,
                                              final @PathVariable Long orderId) {
        orderService.finalizeOrder(request, companyId, orderId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('VIEW_ONLINE_ORDERING')")
    public ResponseEntity<Void> approveNewIncomingOrder(final @RequestBody ApproveNewIncomingOrderRequest request) {
        orderService.approveNewIncomingOrder(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('VIEW_ONLINE_ORDERING')")
    public ResponseEntity<Void> rejectNewIncomingOrder(final @RequestBody RejectNewIncomingOrderRequest request) {
        orderService.rejectNewIncomingOrder(request);
        return ResponseEntity.ok().build();
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
