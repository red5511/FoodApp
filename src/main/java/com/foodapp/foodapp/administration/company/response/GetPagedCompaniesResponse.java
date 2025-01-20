package com.foodapp.foodapp.administration.company.response;

import com.foodapp.foodapp.administration.company.common.CompaniesPagedResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPagedCompaniesResponse {
    private CompaniesPagedResult pagedResult;
}
