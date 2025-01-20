package com.foodapp.foodapp.dashboard.response;

import com.foodapp.foodapp.administration.company.sql.OpenHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyData {
    private Long companyId;
    private String companyName;
    private String companyAddress;
    private OpenHours openHours;
}
