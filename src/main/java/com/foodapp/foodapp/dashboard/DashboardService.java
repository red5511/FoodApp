package com.foodapp.foodapp.dashboard;

import com.foodapp.foodapp.administration.company.CompanyDto;
import com.foodapp.foodapp.administration.company.CompanyMapper;
import com.foodapp.foodapp.administration.company.CompanyService;
import com.foodapp.foodapp.dashboard.request.GetActiveOrdersRequest;
import com.foodapp.foodapp.dashboard.response.DashboardGetCompanyResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetInitConfigResponse;
import com.foodapp.foodapp.dashboard.response.DashboardGetOrdersResponse;
import com.foodapp.foodapp.order.OrderService;
import com.foodapp.foodapp.order.OrderStatus;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.user.permission.PermissionUtils;
import com.foodapp.foodapp.user.permission.PermittedModules;
import lombok.AllArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class DashboardService {
    private final CompanyService companyService;
    private final ContextProvider contextProvider;
    private final OrderService orderService;

    public DashboardGetOrdersResponse getActiveOrders(final Long companyId,
                                                      final GetActiveOrdersRequest request) {
        contextProvider.validateCompanyAccessWithHolding(List.of(companyId));
        var orders = orderService.getOrders(companyId, OrderStatus.IN_EXECUTION, request.getSorts());
        return DashboardGetOrdersResponse.builder()
                .orderList(orders)
                .build();
    }

    public DashboardGetCompanyResponse getCompany(final Long companyId) {
        contextProvider.validateCompanyAccess(List.of(companyId));
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
        var companyDataList = contextProvider.getCompanyList().stream()
                .map(CompanyMapper::toCompanyDto)
                .sorted(Comparator.comparing(CompanyDto::getName))
                .collect(Collectors.toList());

        if (companyDataList.size() > 1) {
            companyDataList.add(createHoldingCompany());
        }
        return DashboardGetInitConfigResponse.builder()
                .companyDataList(companyDataList)
                .permittedModules(getPermittedModules())
                .userId(contextProvider.getUserId().orElseThrow(() -> new SecurityException("No user in context ;/")))
                .build();
    }

    private CompanyDto createHoldingCompany() {
        return CompanyDto.builder()
                .id(ContextProvider.HOLDING_ID)
                .name("Wszystkie Firmy")
                .build();
    }

    private Set<PermittedModules> getPermittedModules() {
        var user = contextProvider.getUser().orElseThrow(() -> new SecurityException("User is not present in the context"));
        return PermissionUtils.getPermittedModules(user.getPermissions());
    }
}
