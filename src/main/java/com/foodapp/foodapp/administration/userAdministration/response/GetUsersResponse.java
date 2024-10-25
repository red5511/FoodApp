package com.foodapp.foodapp.administration.userAdministration.response;

import java.util.List;

import com.foodapp.foodapp.user.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUsersResponse {
    List<UserDto> users;
}
