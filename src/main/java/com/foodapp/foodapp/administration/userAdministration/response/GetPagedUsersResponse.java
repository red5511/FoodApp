package com.foodapp.foodapp.administration.userAdministration.response;

import com.foodapp.foodapp.common.UsersPagedResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPagedUsersResponse {
    private UsersPagedResult pagedResult;
}
