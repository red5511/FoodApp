package com.foodapp.foodapp.company;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompanyDto {
    private Long id;
    private String name;
    private String address;
    private OpenHours openHours;
    private CompanyType companyType;
    private boolean isReceivingOrdersActive;
}
