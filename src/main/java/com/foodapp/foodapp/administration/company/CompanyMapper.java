package com.foodapp.foodapp.administration.company;

import java.util.Collection;
import java.util.Set;
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
                         .webSocketTopicName(company.getWebSocketTopicName())
                         .build();

    }

    public static Set<CompanyDto> toCompaniesDto(final Collection<Company> companies) {
        return companies.stream()
                        .map(CompanyMapper::toCompanyDto)
                        .collect(Collectors.toSet());
    }

    public static GetAllCompaniesResponse toGetAllCompaniesResponse(final Set<CompanyDto> companies) {
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
