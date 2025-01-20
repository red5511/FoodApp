package com.foodapp.foodapp.administration.company;

import com.foodapp.foodapp.administration.company.common.CompanyMapper;
import com.foodapp.foodapp.administration.company.request.*;
import com.foodapp.foodapp.administration.company.response.GetAllCompaniesResponse;
import com.foodapp.foodapp.administration.company.response.GetCompanyDetailsResponse;
import com.foodapp.foodapp.administration.company.response.GetPagedCompaniesResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin-panel/companies")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "CompanyAdministration")
public class CompanyAdministrationController {
    private final CompanyService companyService;
    private final CompanyAdministrationService companyAdministrationService;

    @PostMapping("/save")
    public ResponseEntity<String> saveCompany(final @RequestBody SaveCompanyRequest request) {
        companyService.saveCompany(request);
        return ResponseEntity.ok("Saved");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCompany(final @RequestBody DeleteCompanyRequest request) {
        companyService.deleteCompany(request);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/modify")
    public ResponseEntity<String> modifyCompany(final @RequestBody ModifyCompanyRequest request) {
        companyService.modifyCompany(request);
        return ResponseEntity.ok("Modified");
    }

    @GetMapping("/companies")
    public ResponseEntity<GetAllCompaniesResponse> getAllCompanies() {
        var response = CompanyMapper.toGetAllCompaniesResponse(companyService.getCompaniesDto());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{companyId}/details")
    public ResponseEntity<GetCompanyDetailsResponse> getCompanyDetails(final @PathVariable Long companyId) {
        var response = CompanyMapper.toGetCompanyDetailsResponse(companyService.getCompanyDetails(companyId));
        return ResponseEntity.ok(response);
    }


    @PostMapping("/pages")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR')")
    public ResponseEntity<GetPagedCompaniesResponse> getPagedCompanies(final @RequestBody GetCompanyAdministrationRequest request) {
        var pagedResult = companyAdministrationService.getCompanies(request);
        var response = GetPagedCompaniesResponse.builder()
                .pagedResult(pagedResult)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-or-remove-users-companies")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> addOrRemoveUsersCompanies(final @RequestBody AddOrDeleteUsersCompaniesAdministrationRequest request) {
        companyAdministrationService.addOrRemoveUsersCompanies(request);
        return ResponseEntity.ok().build();
    }
}
