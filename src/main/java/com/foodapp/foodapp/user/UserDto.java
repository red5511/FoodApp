package com.foodapp.foodapp.user;

import com.foodapp.foodapp.administration.company.common.CompanyDto;
import com.foodapp.foodapp.user.permission.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Getter
public class UserDto {
    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    private String firstName;
    @Schema(required = true)
    private String lastName;
    @Schema(required = true)
    private String email;
    @Schema(required = true)
    private String phoneNumber;
    @Schema(required = true)
    private Set<CompanyDto> companies;
    @Schema(required = true)
    private boolean enabled;
    @Schema(required = true)
    private boolean locked;
    @Schema(required = true)
    private LocalDateTime createdDate;
    @Schema(required = true)
    private Set<Permission> permissions;
}
