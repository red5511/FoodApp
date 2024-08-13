package com.foodapp.foodapp.company;

import com.foodapp.foodapp.company.request.DeleteCompanyRequest;
import com.foodapp.foodapp.company.request.ModifyCompanyRequest;
import com.foodapp.foodapp.company.request.SaveCompanyRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/company")
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
}
