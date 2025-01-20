package com.foodapp.foodapp.administration.userAdministration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.administration.userAdministration.request.AddOrDeleteCompaniesUsersAdministrationRequest;
import com.foodapp.foodapp.administration.userAdministration.request.AddOrDeleteUsersPermissionsAdministrationRequest;
import com.foodapp.foodapp.administration.userAdministration.request.GetUsersAdministrationRequest;
import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserDto;
import com.foodapp.foodapp.user.UserRepository;
import com.foodapp.foodapp.user.UsersPagedResult;
import com.foodapp.foodapp.user.permission.Permission;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserAdministrationService {
    private final UserDetailsServiceImpl userDetailsService;
    private final ContextProvider contextProvider;
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public List<UserDto> getAllUsers() {
        return userDetailsService.getDtoUsers();
    }

    public UsersPagedResult getNormalUsers(final GetUsersAdministrationRequest request) {
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

    public Set<Permission> getAllPermissions() {
        contextProvider.validateAdminRights();
        var permissions = new HashSet<>(Set.of(Permission.values()));
        try {
            contextProvider.validateSuperAdminRights();
        } catch(Exception e) {
            permissions.remove(Permission.ADMINISTRATOR);
            permissions.remove(Permission.SUPER_ADMINISTRATOR);
        }
        return permissions;
    }

    public void addOrRemoveUserPermissions(final AddOrDeleteUsersPermissionsAdministrationRequest request) {
        validateUserPermissions(request.getPermissionToAdd());
        validateUserPermissions(request.getPermissionToRemove());

        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new SecurityException("Wrong user Id"));
        user.getPermissions().addAll(request.getPermissionToAdd());
        user.getPermissions().removeAll(request.getPermissionToRemove());

        userRepository.save(user);
    }

    private void validateUserPermissions(final Set<Permission> permissionsToCheck) {
        Set<Permission> adminPermissions = Permission.administrationPermissions();

        for(Permission permission : permissionsToCheck) {
            if(adminPermissions.contains(permission)) {
                contextProvider.validateSuperAdminRights();
                return; // Exit after finding a match, as we only need to print once
            }
        }
        contextProvider.validateAdminRights();

    }

    public void addOrRemoveUsersCompanies(final AddOrDeleteCompaniesUsersAdministrationRequest request) {
        contextProvider.validateAdminRights();
        var company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new SecurityException("Wrong company id"));

        var usersToAdd = userRepository.findAllById(request.getUsersIdsToAdd());
        if(usersToAdd.size() != request.getUsersIdsToAdd().size()) {
            throw new IllegalArgumentException("Some user IDs to add are invalid.");
        }

        var usersToRemove = userRepository.findAllById(request.getUsersIdsToRemove());
        if(usersToRemove.size() != request.getUsersIdsToRemove().size()) {
            throw new IllegalArgumentException("Some user IDs to remove are invalid.");
        }

        usersToAdd.forEach(userToAdd -> {
            userToAdd.getCompanies().add(company);
            company.getUsers().add(userToAdd);
        });
        usersToRemove.forEach(userToRemove -> {
            company.getUsers().remove(userToRemove);
            userToRemove.getCompanies().remove(company);
        });

        companyRepository.save(company);
        userRepository.saveAll(usersToRemove);
        userRepository.saveAll(usersToAdd);
    }
}
