package com.foodapp.foodapp.dashboard;

import com.foodapp.foodapp.company.CompanyDto;
import com.foodapp.foodapp.company.CompanyService;
import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetOrdersResponse;
import com.foodapp.foodapp.order.OrderService;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.security.ContextProvider;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class DashboardService {
    private final CompanyService companyService;
    private final ContextProvider contextValidator;
    private final OrderService orderService;

    public DashboardGetOrdersResponse getActiveOrders(final Long companyId) {
        contextValidator.validateCompanyAccess(companyId);
        var orders = orderService.getOrders(companyId, OrderStatus.IN_EXECUTION);
        return DashboardGetOrdersResponse.builder()
                .orderList(orders)
                .build();
    }

    public DashboardGetCompanyResponse getCompany(final Long companyId) {
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
                .map(company -> CompanyDto.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .address(company.getAddress())
                        .openHours(company.getContent().getOpenHours())
                        .isReceivingOrdersActive(company.isReceivingOrdersActive())
                        .build())
                .toList();
        var companyDataList2 = contextValidator.getCompanyList().stream()
                .map(company -> CompanyDto.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .address(company.getAddress())
                        .openHours(company.getContent().getOpenHours())
                        .isReceivingOrdersActive(company.isReceivingOrdersActive())
                        .build())
                .toList();
        var companyDataList3 = contextValidator.getCompanyList().stream()
                .map(company -> CompanyDto.builder()
                        .id(company.getId())
                        .name(company.getName())
                        .address(company.getAddress())
                        .openHours(company.getContent().getOpenHours())
                        .isReceivingOrdersActive(company.isReceivingOrdersActive())
                        .build())
                .toList();
        List<CompanyDto> xd = new ArrayList<>();
        xd.addAll(companyDataList);
        xd.addAll(companyDataList2);
        return DashboardGetInitConfigResponse.builder()
                .companyDataList(xd)
                .build();
    }
}
//kwtnmdal-95
//kwtnmdal-96