package com.foodapp.foodapp.dashboard.response;

import com.foodapp.foodapp.administration.company.common.CompanyDto;
import com.foodapp.foodapp.user.permission.PermittedModules;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGetInitConfigResponse {
    @Schema(required = true)
    private boolean isReceivingOrdersActive;
    @Schema(required = true)
    private List<CompanyDto> companyDataList;
    @Schema(required = true)
    private Set<PermittedModules> permittedModules;
    @Schema(required = true)
    private Long userId;
}
