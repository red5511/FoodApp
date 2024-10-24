package com.foodapp.foodapp.administration.company.response;

import com.foodapp.foodapp.administration.company.CompanyDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetCompanyDetailsResponse {
    private CompanyDto company;
}
