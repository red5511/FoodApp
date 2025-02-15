package com.foodapp.foodapp.administration.company.sql;

import com.foodapp.foodapp.administration.company.common.CompaniesPagedResult;
import com.foodapp.foodapp.administration.company.common.CompanySearchParams;

public interface CompanyCustomRepository {
    CompaniesPagedResult searchCompanies(CompanySearchParams params);

}
