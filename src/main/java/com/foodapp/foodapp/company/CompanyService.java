package com.foodapp.foodapp.company;

import com.foodapp.foodapp.company.request.DeleteCompanyRequest;
import com.foodapp.foodapp.company.request.ModifyCompanyRequest;
import com.foodapp.foodapp.company.request.SaveCompanyRequest;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;

    public Set<Company> getCompaniesByUserId(final String userEmail) {
        var user = (User) userDetailsService.loadUserByUsername2(userEmail);
        return user.getCompanies();
    }

    public Optional<Company> getCompanyById(final Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Transactional
    public void saveCompany(final SaveCompanyRequest request) {
        Set<User> userSet = new HashSet<>();
        User user = null;
        if (request.getUserEmail() != null) {
            user = (User) userDetailsService.loadUserByUsername(request.getUserEmail());
            userSet.add(user);
        }
        var companyDto = request.getCompany();
        var company = Company.builder()
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .content(Content.builder()
                        .openHours(companyDto.getOpenHours())
                        .build())
                .companyUsers(userSet)
                .build();
        if (user != null) {
            user.getCompanies().add(company);
            userRepository.save(user);
        }
        companyRepository.save(company);
    }

    public void modifyCompany(final ModifyCompanyRequest request) {
        var companyDto = request.getCompanyDto();
        var company = Company.builder()
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .content(Content.builder()
                        .openHours(companyDto.getOpenHours())
                        .build())
                .build();
        companyRepository.save(company);
    }

    public void deleteCompany(final DeleteCompanyRequest request) {
        companyRepository.deleteById(request.getCompanyId());
    }
}
