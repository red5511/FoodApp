package com.foodapp.foodapp.administration.company.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

import com.foodapp.foodapp.administration.company.CompanyDto;

@Builder
@Getter
public class GetAllCompaniesResponse {
    List<CompanyDto> companies;
}
