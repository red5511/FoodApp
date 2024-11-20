package com.foodapp.foodapp.dashboard.response;

import com.foodapp.foodapp.administration.company.CompanyDto;
import com.foodapp.foodapp.user.permission.PermittedModules;
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
    private boolean isReceivingOrdersActive;
    private List<CompanyDto> companyDataList;
    private Set<PermittedModules> permittedModules;
}
