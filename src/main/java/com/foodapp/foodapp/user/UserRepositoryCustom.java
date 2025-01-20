package com.foodapp.foodapp.user;

import com.foodapp.foodapp.administration.userAdministration.UsersSearchParams;

public interface UserRepositoryCustom {
    UsersPagedResult searchUsers(UsersSearchParams params);
}