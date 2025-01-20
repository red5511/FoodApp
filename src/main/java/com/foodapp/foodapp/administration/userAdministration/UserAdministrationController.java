package com.foodapp.foodapp.administration.userAdministration;

import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.foodapp.administration.userAdministration.request.AddOrDeleteCompaniesUsersAdministrationRequest;
import com.foodapp.foodapp.administration.userAdministration.request.AddOrDeleteUsersPermissionsAdministrationRequest;
import com.foodapp.foodapp.administration.userAdministration.request.GetUsersAdministrationRequest;
import com.foodapp.foodapp.administration.userAdministration.response.GetAllPermissionsResponse;
import com.foodapp.foodapp.administration.userAdministration.response.GetPagedUsersResponse;
import com.foodapp.foodapp.administration.userAdministration.response.GetUsersResponse;
import com.foodapp.foodapp.user.permission.Permission;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin-panel/users")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "UserAdministration")
public class UserAdministrationController {

    private final UserAdministrationService userAdministrationService;

    @PostMapping("/pages")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR')")
    public ResponseEntity<GetPagedUsersResponse> getPagedUsers(final @RequestBody GetUsersAdministrationRequest request) {
        var pagedResult = userAdministrationService.getNormalUsers(request);
        var response = GetPagedUsersResponse.builder()
                                            .pagedResult(pagedResult)
                                            .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/permissions")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<GetAllPermissionsResponse> getAllPermissions() {
        Set<Permission> permissions = userAdministrationService.getAllPermissions();
        var response = GetAllPermissionsResponse.builder()
                                                .permissions(permissions)
                                                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users")
    public ResponseEntity<GetUsersResponse> getAllUsers() {
        var response = UserAdministrationMapper.toGetUsersResponse(userAdministrationService.getAllUsers());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{companyId}/users")
    public ResponseEntity<GetUsersResponse> getCompanyUsers(final @PathVariable Long companyId) {
        var response = UserAdministrationMapper.toGetUsersResponse(userAdministrationService.getCompanyUsers(companyId));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{companyId}/users-to-add")
    public ResponseEntity<GetUsersResponse> getUsersNotBelongToCompany(final @PathVariable Long companyId) {
        var response =
            UserAdministrationMapper.toGetUsersResponse(userAdministrationService.getDtoUsersNotBelongToCompany(companyId));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-or-remove-users-permissions")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> addOrRemoveUsersPermissions(
        final @RequestBody AddOrDeleteUsersPermissionsAdministrationRequest request) {
        userAdministrationService.addOrRemoveUserPermissions(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-or-remove-companies-users")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('SUPER_ADMINISTRATOR')")
    public ResponseEntity<Void> addOrRemoveCompaniesUsers(final @RequestBody AddOrDeleteCompaniesUsersAdministrationRequest request) {
        userAdministrationService.addOrRemoveUsersCompanies(request);
        return ResponseEntity.ok().build();
    }
}
