package com.foodapp.foodapp.administration.userAdministration.request;

import java.util.Set;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddOrDeleteCompaniesUsersAdministrationRequest {
    @Schema(required = true)
    private Set<Long> usersIdsToAdd;
    @Schema(required = true)
    private Set<Long> usersIdsToRemove;
    @Schema(required = true)
    @NotEmpty()
    private Long companyId;
}
