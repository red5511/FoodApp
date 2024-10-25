package com.foodapp.foodapp.user;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toUserDto(final User user) {
        return UserDto.builder()
                      .email(user.getEmail())
                      .firstName(user.getFirstName())
                      .lastName(user.getLastName())
                      .id(user.getId())
                      .build();
    }

    public static List<UserDto> toUsersDto(final List<User> users) {
        return users.stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
    }
}
