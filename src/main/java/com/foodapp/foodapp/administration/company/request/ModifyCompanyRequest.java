package com.foodapp.foodapp.administration.company.request;

import com.foodapp.foodapp.administration.company.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModifyCompanyRequest {
    private CompanyDto companyDto;
}
