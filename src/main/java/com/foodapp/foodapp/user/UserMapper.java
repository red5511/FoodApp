package com.foodapp.foodapp.user;

import com.foodapp.foodapp.administration.company.common.CompanyMapper;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toUserDto(final User user) {
        var builder = UserDto.builder();
        toCommonMap(builder, user);
        return builder.companies(CompanyMapper.toCompaniesDtoWithoutUsers(user.getCompanies()))
                .build();
    }

    public static UserDto toUserDtoWithoutCompanies(final User user) {
        var builder = UserDto.builder();
        toCommonMap(builder, user);
        return builder.build();
    }

    public static List<UserDto> toUsersDto(final Collection<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    public static List<UserDto> toUsersDtoWithoutCompanies(final Collection<User> users) {
        return users.stream()
                .map(UserMapper::toUserDtoWithoutCompanies)
                .collect(Collectors.toList());
    }

    private static void toCommonMap(final UserDto.UserDtoBuilder builder, final User user) {
        builder.id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .locked(user.getLocked())
                .enabled(user.getEnabled())
                .createdDate(user.getCreatedDate())
                .permissions(user.getPermissions());
    }
}
