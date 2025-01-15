package com.foodapp.foodapp.administration.userAdministration.request;

import com.foodapp.foodapp.common.BasePagedRequest;
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
public class GetUsersAdministrationRequest extends BasePagedRequest {
    private String globalSearch;
}
