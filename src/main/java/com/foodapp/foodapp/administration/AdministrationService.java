package com.foodapp.foodapp.administration;

import com.foodapp.foodapp.administration.company.Company;
import com.foodapp.foodapp.administration.company.CompanyRepository;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AdministrationService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public void addUserToCompany(final Long companyId, final Long userId) {
        Company company = companyRepository.findById(companyId)
                                           .orElseThrow(() -> new SecurityException("Company not found"));
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new SecurityException("User not found"));

        company.getCompanyUsers().add(user);
        user.getCompanies().add(company);

        companyRepository.save(company);
        userRepository.save(user);
    }
}
