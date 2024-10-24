package com.foodapp.foodapp.administration.company;

import java.util.List;
import java.util.stream.Collectors;

import com.foodapp.foodapp.administration.company.response.GetAllCompaniesResponse;
import com.foodapp.foodapp.administration.company.response.GetCompanyDetailsResponse;

public class CompanyMapper {

    public static CompanyDto toCompanyDto(final Company company) {
        return CompanyDto.builder()
                         .id(company.getId())
                         .name(company.getName())
                         .address(company.getAddress())
                         .openHours(company.getContent().getOpenHours())
                         .isReceivingOrdersActive(company.isReceivingOrdersActive())
                         .build();

    }

    public static List<CompanyDto> toCompaniesDto(final List<Company> companies) {
        return companies.stream()
                        .map(CompanyMapper::toCompanyDto)
                        .collect(Collectors.toList());
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
}
