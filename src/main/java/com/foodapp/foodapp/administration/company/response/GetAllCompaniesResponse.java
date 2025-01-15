package com.foodapp.foodapp.administration.company.response;

import java.util.Set;

import com.foodapp.foodapp.administration.company.CompanyDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class GetAllCompaniesResponse {
    Set<CompanyDto> companies;
}
