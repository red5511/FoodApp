package com.foodapp.foodapp.user.permission;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionUtils {
    private static final Map<Permission, PermittedModules> PERMISSION_PERMITTED_MODULES_MAP = Map.of(
            Permission.VIEW_ORDERS, PermittedModules.ORDERS,
            Permission.VIEW_LIVE_PANEL, PermittedModules.LIVE_PANEL,
            Permission.VIEW_STATISTICS, PermittedModules.STATISTICS,
            Permission.VIEW_RESTAURANT_ORDER, PermittedModules.RESTAURANT_ORDER);

    public static Set<PermittedModules> getPermittedModules(final Set<Permission> permissions) {
        return permissions.stream()
                .map(PERMISSION_PERMITTED_MODULES_MAP::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
