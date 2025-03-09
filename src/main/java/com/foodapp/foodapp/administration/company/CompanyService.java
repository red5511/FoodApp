package com.foodapp.foodapp.administration.company;

import com.foodapp.foodapp.administration.company.common.CompanyDto;
import com.foodapp.foodapp.administration.company.common.CompanyMapper;
import com.foodapp.foodapp.administration.company.request.DeleteCompanyRequest;
import com.foodapp.foodapp.administration.company.request.ModifyCompanyRequest;
import com.foodapp.foodapp.administration.company.request.SaveCompanyRequest;
import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.administration.company.sql.Content;
import com.foodapp.foodapp.user.User;
import com.foodapp.foodapp.user.UserDetailsServiceImpl;
import com.foodapp.foodapp.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;

    public Set<Company> getCompaniesByUserId(final String userEmail) {
        var user = (User) userDetailsService.loadUserByUsername2(userEmail);
        return user.getCompanies();
    }

    public Optional<Company> getCompanyById(final Long companyId) {
        return companyRepository.findById(companyId);
    }

    @Transactional
    public Long saveCompany(final SaveCompanyRequest request) {
        var company = companyMapper.toCompany(request.getCompany());
        companyRepository.save(company);
        return company.getId();
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

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public List<CompanyDto> getCompaniesDto() {
        return CompanyMapper.toCompaniesDto(getCompanies());
    }

    public CompanyDto getCompanyDetails(final Long companyId) {
        var companyOptional = companyRepository.findById(companyId);
        if (companyOptional.isEmpty()) {
            throw new SecurityException("Wrong company id");
        }
        return CompanyMapper.toCompanyDto(companyOptional.get());
    }
}
