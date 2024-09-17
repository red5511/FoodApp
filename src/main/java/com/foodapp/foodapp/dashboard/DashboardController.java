package com.foodapp.foodapp.dashboard;

import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetOrdersResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DashboardGetCompanyResponse> getCompany(final @PathVariable Integer companyId) {
        return ResponseEntity.ok(dashboardService.getCompany(companyId));
    }
//
//    @PostMapping
//    public ResponseEntity<DashboardGetOrdersResponse> getOrders(final @PathVariable Integer companyId) {
//        return ResponseEntity.ok(dashboardService.getOrders(companyId));
//    }
}
