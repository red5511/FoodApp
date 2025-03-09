package com.foodapp.foodapp.administration.company.common;

import com.foodapp.foodapp.administration.company.response.GetAllCompaniesResponse;
import com.foodapp.foodapp.administration.company.response.GetCompanyDetailsResponse;
import com.foodapp.foodapp.administration.company.sql.Company;
import com.foodapp.foodapp.administration.company.sql.CompanyRepository;
import com.foodapp.foodapp.administration.company.sql.Content;
import com.foodapp.foodapp.common.CommonUtils;
import com.foodapp.foodapp.user.UserMapper;
import lombok.AllArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CompanyMapper {
    private final CompanyRepository companyRepository;


    public static Set<CompanyDto> toCompaniesDtoWithoutUsers(final Collection<Company> companies) {
        return companies.stream()
                .map(CompanyMapper::toCompanyDtoWithoutUsers)
                .collect(Collectors.toSet());
    }

    public static CompanyDto toCompanyDtoWithoutUsers(final Company company) {
        var builder = CompanyDto.builder();
        mapCommon(builder, company);
        return builder.build();
    }

    public static List<CompanyDto> toCompaniesDto(final Collection<Company> companies) {
        return companies.stream()
                .map(CompanyMapper::toCompanyDto)
                .collect(Collectors.toList());
    }

    public static CompanyDto toCompanyDto(final Company company) {
        var builder = CompanyDto.builder();
        mapCommon(builder, company);
        return builder
                .users(UserMapper.toUsersDtoWithoutCompanies(company.getUsers()))
                .build();

    }

    public static GetAllCompaniesResponse toGetAllCompaniesResponse(final List<CompanyDto> companies) {
        return GetAllCompaniesResponse.builder()
                .companies(companies)
                .build();
    }

    public static GetCompanyDetailsResponse toGetCompanyDetailsResponse(final CompanyDto companyDto) {
        return GetCompanyDetailsResponse.builder()
                .company(companyDto)
                .build();
    }

    private static void mapCommon(final CompanyDto.CompanyDtoBuilder builder, final Company company) {
        builder.id(company.getId())
                .name(company.getName())
                .address(company.getAddress())
                .openHours(company.getContent().getOpenHours())
                .webSocketTopicName(company.getWebSocketTopicName())
                .defaultProductImgUrl(company.getDefaultProductImgUrl())
                .logoUrl(company.getLogoUrl())
                .createdDate(company.getCreatedDate());
    }

    public Company toCompany(final CompanyDto companyDto) {
        var company = Company.builder()
                .name(companyDto.getName())
                .address(companyDto.getAddress())
                .logoUrl(companyDto.getLogoUrl())
                .content(Content.builder()
                        .openHours(companyDto.getOpenHours())
                        .build())
                .build();
        company = companyRepository.save(company);
        company.setDefaultProductImgUrl(CommonUtils.createDefaultProductImgUrl(company.getId().toString()));
        company.setWebSocketTopicName(company.getId() + "_" + UUID.randomUUID().toString().substring(0, 8));
        return company;
    }
}
