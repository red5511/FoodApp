package com.foodapp.foodapp.dashboard;

import java.util.Comparator;
import java.util.Set;

import com.foodapp.foodapp.administration.company.CompanyDto;
import com.foodapp.foodapp.administration.company.CompanyMapper;
import com.foodapp.foodapp.administration.company.CompanyService;
import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetOrdersResponse;
import com.foodapp.foodapp.order.OrderService;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.user.permission.PermissionUtils;
import com.foodapp.foodapp.user.permission.PermittedModules;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DashboardService {
    private final CompanyService companyService;
    private final ContextProvider contextProvider;
    private final OrderService orderService;

    public DashboardGetOrdersResponse getActiveOrders(final Long companyId) {
        contextProvider.validateCompanyAccess(companyId);
        var orders = orderService.getOrders(companyId, OrderStatus.IN_EXECUTION);
        return DashboardGetOrdersResponse.builder()
                                         .orderList(orders)
                                         .build();
    }

    public DashboardGetCompanyResponse getCompany(final Long companyId) {
        contextProvider.validateCompanyAccess(companyId);
        var companyOptional = companyService.getCompanyById(companyId);
        if(companyOptional.isPresent()) {
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
        var companyDataList = contextProvider.getCompanyList().stream()
                                             .map(CompanyMapper::toCompanyDto)
                                             .sorted(Comparator.comparing(CompanyDto::getName))
                                             .toList();
        return DashboardGetInitConfigResponse.builder()
                                             .companyDataList(companyDataList)
                                             .permittedModules(getPermittedModules())
                                             .build();
    }

    private Set<PermittedModules> getPermittedModules() {
        var user = contextProvider.getUser().orElseThrow(() -> new SecurityException("User is not present in the context"));
        return PermissionUtils.getPermittedModules(user.getPermissions());
    }
}
