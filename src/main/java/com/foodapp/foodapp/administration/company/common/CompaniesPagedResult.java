package com.foodapp.foodapp.administration.company.common;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CompaniesPagedResult {
    private List<CompanyDto> companies;
    private long totalRecords;
}
