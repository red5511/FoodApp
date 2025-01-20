package com.foodapp.foodapp.administration.company.request;


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
public class AddOrDeleteUsersCompaniesAdministrationRequest {
    @Schema(required = true)
    private Set<Long> companyIdsToAdd;
    @Schema(required = true)
    private Set<Long> companyIdsToRemove;
    @Schema(required = true)
    @NotEmpty()
    private Long userId;
}
