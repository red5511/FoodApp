package com.foodapp.foodapp.dashboard.response;

import com.foodapp.foodapp.administration.company.sql.OpenHours;
import com.foodapp.foodapp.common.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGetCompanyResponse {
    private String companyName;
    private Address companyAddress;
    private OpenHours openHours;
}
