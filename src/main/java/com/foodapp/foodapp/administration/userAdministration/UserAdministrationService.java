package com.foodapp.foodapp.administration.userAdministration;

import com.foodapp.foodapp.administration.userAdministration.request.GetUsersAdministrationRequest;
import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.common.UsersPagedResult;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserDto;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class UserAdministrationService {
    private final UserDetailsServiceImpl userDetailsService;
    private final ContextProvider contextProvider;

    public List<UserDto> getAllUsers() {
        return userDetailsService.getDtoUsers();
    }

    public UsersPagedResult getAllUsers(final GetUsersAdministrationRequest request) {
        contextProvider.validateSuperAdminRights();
        var searchParams = CommonMapper.mapToSearchParams(request);
        return userDetailsService.getDtoUsersBySearchParams(searchParams);
    }

    public List<UserDto> getCompanyUsers(final Long companyId) {
        return userDetailsService.getDtoUsers(companyId);
    }

    public List<UserDto> getDtoUsersNotBelongToCompany(final Long companyId) {
        return userDetailsService.getDtoUsersNotBelongToCompany(companyId);
    }
}
