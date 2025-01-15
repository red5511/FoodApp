package com.foodapp.foodapp.administration.userAdministration;

import com.foodapp.foodapp.common.SearchParams;
import com.foodapp.foodapp.user.Role;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class UsersSearchParams extends SearchParams {
    private String global;
    private Role role;
}
