package com.foodapp.foodapp.dashboard;

import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
