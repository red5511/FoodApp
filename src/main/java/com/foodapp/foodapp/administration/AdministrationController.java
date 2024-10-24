package com.foodapp.foodapp.administration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/administration")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Administration")
public class AdministrationController {
    private final AdministrationService administrationService;

    @PutMapping("/company/{companyId}/user/{userId}")
    public ResponseEntity<Void> addUserToCompany(final @PathVariable Long companyId, final @PathVariable Long userId) {
        administrationService.addUserToCompany(companyId, userId);
        return ResponseEntity.ok().build();
    }

}
