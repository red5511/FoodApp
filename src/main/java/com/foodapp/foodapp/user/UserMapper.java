package com.foodapp.foodapp.user;

import com.foodapp.foodapp.administration.company.CompanyMapper;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toUserDto(final User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .locked(user.getLocked())
                .enabled(user.getEnabled())
                .companies(CompanyMapper.toCompaniesDto(user.getCompanies()))
                .createdDate(user.getCreatedDate())
                .permissions(user.getPermissions())
                .build();
    }

    public static List<UserDto> toUsersDto(final List<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
