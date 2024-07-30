package com.foodapp.foodapp.company.request;

import com.foodapp.foodapp.company.OpenHours;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaveCompanyRequest {
    private String name;
    private String address;
    private OpenHours openHours;
}
