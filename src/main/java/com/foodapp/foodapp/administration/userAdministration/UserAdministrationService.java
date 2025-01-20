package com.foodapp.foodapp.administration.userAdministration;

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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class UserAdministrationService {
    private final UserDetailsServiceImpl userDetailsService;
    private final ContextProvider contextProvider;
    private final UserRepository userRepository;

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
        } catch (Exception e) {
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

        for (Permission permission : permissionsToCheck) {
            if (adminPermissions.contains(permission)) {
                contextProvider.validateSuperAdminRights();
                return; // Exit after finding a match, as we only need to print once
            }
        }
        contextProvider.validateAdminRights();

    }
}
