package com.foodapp.foodapp.administration.userAdministration;

import com.foodapp.foodapp.common.SearchParams;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UsersSearchParams extends SearchParams {
    private String global;
}
