package com.foodapp.foodapp.administration.company.request;

import com.foodapp.foodapp.administration.company.CompanyDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCompanyRequest {
    private CompanyDto company;
    private String userEmail;
}
