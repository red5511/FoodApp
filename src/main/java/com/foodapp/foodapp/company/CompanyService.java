package com.foodapp.foodapp.company;

import com.foodapp.foodapp.company.request.DeleteCompanyRequest;
import com.foodapp.foodapp.company.request.ModifyCompanyRequest;
import com.foodapp.foodapp.company.request.SaveCompanyRequest;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public void saveCompany(final SaveCompanyRequest request) {
        var company = Company.builder()
                .name(request.getName())
                .address(request.getAddress())
                .content(Content.builder()
                        .openHours(request.getOpenHours())
                        .build())
                .createdOn(LocalDateTime.now())
                .build();
        companyRepository.save(company);
    }

    public void modifyCompany(final ModifyCompanyRequest request) {
        var company = Company.builder()
                .name(request.getName())
                .address(request.getAddress())
                .content(Content.builder()
                        .openHours(request.getOpenHours())
                        .build())
                .modifiedOn(LocalDateTime.now())
                .build();
        companyRepository.save(company);
    }

    public void deleteCompany(final DeleteCompanyRequest request) {
        companyRepository.deleteById(request.getCompanyId());
    }
}
