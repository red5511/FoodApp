package com.foodapp.foodapp.administration.company;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.foodapp.administration.company.request.DeleteCompanyRequest;
import com.foodapp.foodapp.administration.company.request.ModifyCompanyRequest;
import com.foodapp.foodapp.administration.company.request.SaveCompanyRequest;
import com.foodapp.foodapp.administration.company.response.GetAllCompaniesResponse;
import com.foodapp.foodapp.administration.company.response.GetCompanyDetailsResponse;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/administration/company")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Company")
public class CompanyController {
    private final CompanyService companyService;

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
}
