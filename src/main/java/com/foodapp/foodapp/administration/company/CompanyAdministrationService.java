package com.foodapp.foodapp.administration.company;

import com.foodapp.foodapp.administration.company.common.CompaniesPagedResult;
import com.foodapp.foodapp.administration.company.request.AddOrDeleteUsersCompaniesAdministrationRequest;
import com.foodapp.foodapp.administration.company.request.GetCompanyAdministrationRequest;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.common.CommonMapper;
import com.foodapp.foodapp.security.ContextProvider;
import com.foodapp.foodapp.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
public class CompanyAdministrationService {
    private final ContextProvider contextProvider;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;


    public CompaniesPagedResult getCompanies(final GetCompanyAdministrationRequest request) {
        contextProvider.validateSuperAdminRights();
        var searchParams = CommonMapper.mapToSearchParams(request);
        return companyRepository.searchCompanies(searchParams);
    }

    @Transactional
    public void addOrRemoveUsersCompanies(final AddOrDeleteUsersCompaniesAdministrationRequest request) {
        contextProvider.validateAdminRights();
        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new SecurityException("Wrong user id"));

        var companiesToAdd = companyRepository.findAllById(request.getCompanyIdsToAdd());
        if (companiesToAdd.size() != request.getCompanyIdsToAdd().size()) {
            throw new IllegalArgumentException("Some company IDs to add are invalid.");
        }

        var companiesToRemove = companyRepository.findAllById(request.getCompanyIdsToRemove());
        if (companiesToRemove.size() != request.getCompanyIdsToRemove().size()) {
            throw new IllegalArgumentException("Some company IDs to remove are invalid.");
        }

        companiesToAdd.forEach(company -> {
            company.getUsers().add(user);
            user.getCompanies().add(company);
        });
        companiesToRemove.forEach(company -> {
            company.getUsers().remove(user);
            user.getCompanies().remove(company);
        });

        userRepository.save(user);
        companyRepository.saveAll(companiesToAdd);
        companyRepository.saveAll(companiesToRemove);
    }
}
