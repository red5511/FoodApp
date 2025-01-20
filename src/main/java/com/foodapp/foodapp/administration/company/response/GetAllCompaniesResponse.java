package com.foodapp.foodapp.administration.company.response;

import com.foodapp.foodapp.administration.company.common.CompanyDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class GetAllCompaniesResponse {
    List<CompanyDto> companies;
}
