package com.foodapp.foodapp.administration.cache;

import com.foodapp.foodapp.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class UsersConnectedToWebSocketEntry {
    public UserDto user;
    public Long companyId;
}
