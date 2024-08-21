package com.foodapp.foodapp.dashboard;

import com.foodapp.foodapp.company.Company;
import com.foodapp.foodapp.company.CompanyService;
import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;

import java.util.stream.Collectors;

@AllArgsConstructor
public class DashboardService {
    private final CompanyService companyService;
    private final ContextProvider contextValidator;

    public DashboardGetCompanyResponse getCompany(final Integer companyId) {
        contextValidator.validateCompanyAccess(companyId);
        var companyOptional = companyService.getCompanyById(companyId);
        if (companyOptional.isPresent()) {
            var company = companyOptional.get();
            return DashboardGetCompanyResponse.builder()
                    .companyName(company.getName())
                    .companyAddress(company.getAddress())
                    .openHours(company.getContent().getOpenHours())
                    .build();
        }
        return null;
    }

    public DashboardGetInitConfigResponse getInitConfig() {
        var companyIds = contextValidator.getCompanyList().stream()
                .map(Company::getId)
                .collect(Collectors.toSet());
        return DashboardGetInitConfigResponse.builder()
                .companyIds(companyIds)
                .build();
    }
}
