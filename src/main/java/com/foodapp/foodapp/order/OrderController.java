package com.foodapp.foodapp.order;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.order.request.ApproveNewIncomingOrderRequest;
import com.foodapp.foodapp.order.request.CreateOrderRequest;
import com.foodapp.foodapp.order.request.GetOrdersConfigRequest;
import com.foodapp.foodapp.order.request.GetOrdersForCompanyRequest;
import com.foodapp.foodapp.order.request.RejectNewIncomingOrderRequest;
import com.foodapp.foodapp.order.response.GetOrdersConfigResponse;
import com.foodapp.foodapp.order.response.PagedOrdersResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

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
    @PreAuthorize("hasAuthority('VIEW_PANEL_LIVE')")
    public ResponseEntity<Void> approveNewIncomingOrder(final @RequestBody ApproveNewIncomingOrderRequest request) {
        orderService.approveNewIncomingOrder(request);
        return ResponseEntity.ok().build(); // Return 200 OK with no body
    }

    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('VIEW_PANEL_LIVE')")
    public ResponseEntity<Void> rejectNewIncomingOrder(final @RequestBody RejectNewIncomingOrderRequest request) {
        orderService.rejectNewIncomingOrder(request);
        return ResponseEntity.ok().build(); // Return 200 OK with no body
    }

    @PostMapping("/orders")
    @PreAuthorize("hasAuthority('VIEW_ORDERS')")
    public ResponseEntity<PagedOrdersResponse> getOrdersForCompany(final @RequestBody GetOrdersForCompanyRequest request) {
        var searchParams =
            CommonMapper.mapToSearchParams(request.getFilters(), request.getSize(), request.getPage(), request.getCompanyId(), request.getSorts());
        var pagedResult = orderService.getOrders(searchParams);
        var response = PagedOrdersResponse.builder()
                                          .pagedResult(pagedResult)
                                          .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/config")
    @PreAuthorize("hasAuthority('VIEW_PANEL_LIVE') or hasAuthority('VIEW_ORDERS')")
    public ResponseEntity<GetOrdersConfigResponse> getOrdersConfig(@RequestBody final GetOrdersConfigRequest request) {
        return ResponseEntity.ok(GetOrdersConfigResponse.builder()
                                                        .orderStatusModels(OrderMapper.getStatusModels())
                                                        .statusSeverityMap(OrderMapper.SEVERITY_TO_ORDER_STATUS_MAP)
                                                        .build());
    }
}
