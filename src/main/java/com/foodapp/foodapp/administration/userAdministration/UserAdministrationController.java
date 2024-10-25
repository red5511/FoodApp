package com.foodapp.foodapp.administration.userAdministration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.foodapp.administration.userAdministration.response.GetUsersResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/administration/user")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "UserAdministration")
public class UserAdministrationController {

    private final UserAdministrationService userAdministrationService;

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
        var response = UserAdministrationMapper.toGetUsersResponse(userAdministrationService.getDtoUsersNotBelongToCompany(companyId));
        return ResponseEntity.ok(response);
    }

}
