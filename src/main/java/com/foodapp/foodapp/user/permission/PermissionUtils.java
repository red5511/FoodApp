package com.foodapp.foodapp.user.permission;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class PermissionUtils {
    private static final Map<Permission, Set<PermittedModules>> PERMISSION_PERMITTED_MODULES_MAP = Map.of(
            Permission.VIEW_ORDERS, Set.of(PermittedModules.ORDERS),
            Permission.VIEW_LIVE_PANEL, Set.of(PermittedModules.LIVE_PANEL),
            Permission.VIEW_STATISTICS, Set.of(PermittedModules.STATISTICS),
            Permission.VIEW_RESTAURANT_ORDER, Set.of(PermittedModules.RESTAURANT_ORDER),
            Permission.SUPER_ADMINISTRATOR, Set.of(PermittedModules.SUPER_ADMIN_PANEL, PermittedModules.ADMIN_PANEL));

    public static Set<PermittedModules> getPermittedModules(final Set<Permission> permissions) {
        return permissions.stream()
                .map(PERMISSION_PERMITTED_MODULES_MAP::get)
                .filter(Objects::nonNull)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }
}
