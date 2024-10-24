package com.foodapp.foodapp.administration.userAdministration;

import java.util.List;

import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserDto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAdministrationService {
    private final UserDetailsServiceImpl userDetailsService;

    public List<UserDto> getAllUsers() {
        return userDetailsService.getDtoUsers();
    }

    public List<UserDto> getCompanyUsers(final Long companyId) {
        return userDetailsService.getDtoUsers(companyId);
    }

    public List<UserDto> getDtoUsersNotBelongToCompany(final Long companyId) {
        return userDetailsService.getDtoUsersNotBelongToCompany(companyId);
    }
}
