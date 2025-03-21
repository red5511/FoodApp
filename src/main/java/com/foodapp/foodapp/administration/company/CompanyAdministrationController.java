package com.foodapp.foodapp.administration.company;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.foodapp.administration.company.common.CompanyMapper;
import com.foodapp.foodapp.administration.company.request.AddOrDeleteUsersCompaniesAdministrationRequest;
import com.foodapp.foodapp.administration.company.request.DeleteCompanyRequest;
import com.foodapp.foodapp.administration.company.request.GetCompanyAdministrationRequest;
import com.foodapp.foodapp.administration.company.request.ModifyCompanyRequest;
import com.foodapp.foodapp.administration.company.request.SaveCompanyRequest;
import com.foodapp.foodapp.administration.company.response.GetAllCompaniesResponse;
import com.foodapp.foodapp.administration.company.response.GetCompanyDetailsResponse;
import com.foodapp.foodapp.administration.company.response.GetPagedCompaniesResponse;
import com.foodapp.foodapp.administration.company.response.SaveCompanyResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/admin-panel/companies")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "CompanyAdministration")
public class CompanyAdministrationController {
    private final CompanyService companyService;
    private final CompanyAdministrationService companyAdministrationService;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<SaveCompanyResponse> saveCompany(final @RequestBody SaveCompanyRequest request) {
        var id = companyService.saveCompany(request);
        var response = SaveCompanyResponse.builder().id(id).build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<String> deleteCompany(final @RequestBody DeleteCompanyRequest request) {
        companyService.deleteCompany(request);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/modify")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<String> modifyCompany(final @RequestBody ModifyCompanyRequest request) {
        companyService.modifyCompany(request);
        return ResponseEntity.ok("Modified");
    }

    @GetMapping("/companies")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<GetAllCompaniesResponse> getAllCompanies() {
        var response = CompanyMapper.toGetAllCompaniesResponse(companyService.getCompaniesDto());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{companyId}/details")
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
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
    @PreAuthorize("hasAuthority('SUPER_ADMINISTRATOR') or hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> addOrRemoveUsersCompanies(final @RequestBody AddOrDeleteUsersCompaniesAdministrationRequest request) {
        companyAdministrationService.addOrRemoveUsersCompanies(request);
        return ResponseEntity.ok().build();
    }
}
