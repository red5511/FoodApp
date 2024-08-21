package com.foodapp.foodapp.dashboard.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardGetInitConfigResponse {
    private Set<Integer> companyIds;
}
