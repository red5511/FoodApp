package com.foodapp.foodapp.user;

import com.foodapp.foodapp.user.permission.Permission;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;


@Converter(autoApply = true)
public class PermissionConverter implements AttributeConverter<Permission, String> {

    @Override
    public String convertToDatabaseColumn(Permission permission) {
        return permission != null ? permission.name() : null;
    }

    @Override
    public Permission convertToEntityAttribute(String permission) {
        return permission != null ? Permission.valueOf(permission) : null;
    }
}