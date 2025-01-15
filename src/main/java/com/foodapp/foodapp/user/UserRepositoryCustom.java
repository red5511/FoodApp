package com.foodapp.foodapp.user;

import com.foodapp.foodapp.administration.userAdministration.UsersSearchParams;
import com.foodapp.foodapp.common.UsersPagedResult;

public interface UserRepositoryCustom {
    UsersPagedResult searchOrders(UsersSearchParams params);
}