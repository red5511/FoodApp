package com.foodapp.foodapp.dashboard.response;

import com.foodapp.foodapp.administration.company.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGetInitConfigResponse {
    private boolean isReceivingOrdersActive;
    private List<CompanyDto> companyDataList;
}
