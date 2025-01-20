package com.foodapp.foodapp.administration.userAdministration.request;


import com.foodapp.foodapp.user.permission.Permission;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddOrDeleteUsersPermissionsAdministrationRequest {
    @Schema(required = true)
    private Set<Permission> permissionToAdd;
    @Schema(required = true)
    private Set<Permission> permissionToRemove;
    @Schema(required = true)
    @NotEmpty()
    private Long userId;
}
