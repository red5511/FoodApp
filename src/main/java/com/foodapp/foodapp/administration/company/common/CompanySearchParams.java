package com.foodapp.foodapp.administration.company.common;

import com.foodapp.foodapp.common.SearchParams;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CompanySearchParams extends SearchParams {
    private String global;
}
