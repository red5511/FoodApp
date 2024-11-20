package com.foodapp.foodapp.dashboard;

import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetOrdersResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/dashboard")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/config/")
    public ResponseEntity<DashboardGetInitConfigResponse> getConfig() {
        return ResponseEntity.ok(dashboardService.getInitConfig());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<DashboardGetCompanyResponse> getCompany(final @PathVariable Long companyId) {
        return ResponseEntity.ok(dashboardService.getCompany(companyId));
    }

    @GetMapping("/orders/{companyId}")
    @PreAuthorize("hasAuthority('VIEW_LIVE_PANEL')")
    public ResponseEntity<DashboardGetOrdersResponse> getActiveOrders(final @PathVariable Long companyId) {
        return ResponseEntity.ok(dashboardService.getActiveOrders(companyId));
    }
//
//    @GetMapping("/order/{companyId}")
//    public ResponseEntity<DashboardGetOrdersResponse> getOrderDetails(final @PathVariable Integer companyId) {
//        return ResponseEntity.ok(dashboardService.getActiveOrders(companyId));
//    }
}
