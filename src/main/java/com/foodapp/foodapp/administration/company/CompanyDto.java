package com.foodapp.foodapp.administration.company;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompanyDto {
    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    private String name;
    @Schema(required = true)
    private String address;
    private OpenHours openHours;
    private CompanyType companyType;
    private boolean isReceivingOrdersActive;
    @Schema(required = true)
    private String webSocketTopicName;
}
