package com.foodapp.foodapp.administration.userAdministration.response;

import com.foodapp.foodapp.user.permission.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllPermissionsResponse {
    @Schema(required = true)
    private Set<Permission> permissions;
}
