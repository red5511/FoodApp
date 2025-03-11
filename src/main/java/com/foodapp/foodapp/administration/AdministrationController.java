package com.foodapp.foodapp.administration;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/administration")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Administration")
public class AdministrationController {
    private final AdministrationService administrationService;

    @PutMapping("/company/{companyId}/user/{userId}")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> addUserToCompany(final @PathVariable Long companyId, final @PathVariable Long userId) {
        administrationService.addUserToCompany(companyId, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/copy-menu/{fromCompanyId}/to/{toCompanyId}")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> copyMenu(final @PathVariable Long fromCompanyId,
                                         final @PathVariable Long toCompanyId) {
        administrationService.copyMenu(fromCompanyId, toCompanyId);
        return ResponseEntity.ok().build();
    }
}
