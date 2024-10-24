package com.foodapp.foodapp.administration.userAdministration;

import java.util.List;

import com.foodapp.foodapp.administration.userAdministration.response.GetUsersResponse;
import com.foodapp.foodapp.user.UserDto;

public class UserAdministrationMapper {
    public static GetUsersResponse toGetUsersResponse(final List<UserDto> users) {
        return GetUsersResponse.builder()
                               .users(users)
                               .build();
    }
}
