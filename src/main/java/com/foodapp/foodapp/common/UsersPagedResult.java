package com.foodapp.foodapp.common;

import com.foodapp.foodapp.user.UserDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UsersPagedResult {
    private List<UserDto> users;
    private long totalRecords;
}
