package com.foodapp.foodapp.company.request;

import com.foodapp.foodapp.company.OpenHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModifyCompanyRequest {
    private String name;
    private String address;
    private OpenHours openHours;
}
