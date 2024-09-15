package com.foodapp.foodapp.dashboard;

import com.foodapp.foodapp.company.CompanyService;
import com.foodapp.foodapp.dashboard.response.CompanyData;
import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
        var companyDataList = contextValidator.getCompanyList().stream()
                .map(company -> CompanyData.builder()
                        .companyId(company.getId())
                        .companyName(company.getName())
                        .companyAddress(company.getAddress())
                        .openHours(company.getContent().getOpenHours())
                        .build())
                .toList();
        var companyDataList2 = contextValidator.getCompanyList().stream()
                .map(company -> CompanyData.builder()
                        .companyId(company.getId())
                        .companyName(company.getName())
                        .companyAddress(company.getAddress())
                        .openHours(company.getContent().getOpenHours())
                        .build())
                .toList();
        var companyDataList3 = contextValidator.getCompanyList().stream()
                .map(company -> CompanyData.builder()
                        .companyId(company.getId())
                        .companyName(company.getName())
                        .companyAddress(company.getAddress())
                        .openHours(company.getContent().getOpenHours())
                        .build())
                .toList();
        List<CompanyData> xd = new ArrayList<>();
        xd.addAll(companyDataList);
        xd.addAll(companyDataList2);
        return DashboardGetInitConfigResponse.builder()
                .companyDataList(xd)
                .build();
    }
}
