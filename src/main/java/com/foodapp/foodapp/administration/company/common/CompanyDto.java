package com.foodapp.foodapp.administration.company.common;

import com.foodapp.foodapp.administration.company.sql.OpenHours;
import com.foodapp.foodapp.common.Address;
import com.foodapp.foodapp.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Builder
@Getter
public class CompanyDto {
    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    private String name;
    private OpenHours openHours;
    @Schema(required = true)
    private Address address;
    private CompanyType companyType;
    private boolean isReceivingOrdersActive;
    @Schema(required = true)
    private String webSocketTopicName;
    private boolean isHolding;
    @Schema(required = true)
    private LocalDateTime createdDate;
    @Schema(required = true)
    private List<UserDto> users;
}
