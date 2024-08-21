package com.foodapp.foodapp.company;

import com.foodapp.foodapp.company.request.DeleteCompanyRequest;
import com.foodapp.foodapp.company.request.ModifyCompanyRequest;
import com.foodapp.foodapp.company.request.SaveCompanyRequest;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserDetailsServiceImpl userDetailsService;

    public Set<Company> getCompaniesByUserId(final String userEmail) {
        var user = (User) userDetailsService.loadUserByUsername2(userEmail);
        return user.getCompanies();
    }

    public Optional<Company> getCompanyById(final Integer companyId) {
        return companyRepository.findById(companyId);
    }

    public void saveCompany(final SaveCompanyRequest request) {
        Set<User> userSet = new HashSet<>();
        if (request.getUserEmail() != null) {
            var user = (User) userDetailsService.loadUserByUsername(request.getUserEmail());
            userSet.add(user);
        }
        var company = Company.builder()
                .name(request.getName())
                .address(request.getAddress())
                .content(Content.builder()
                        .openHours(request.getOpenHours())
                        .build())
                .createdOn(LocalDateTime.now())
                .companyUsers(userSet)
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
