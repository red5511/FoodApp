package com.foodapp.foodapp.user.permission;

import java.util.EnumSet;
import java.util.Set;

public enum Permission {
    VIEW_ONLINE_ORDERING,
    VIEW_STATISTICS,
    VIEW_ORDERS_HISTORY,
    VIEW_RESTAURANT_ORDERING,
    ADMINISTRATOR,
    SUPER_ADMINISTRATOR;

    public static Set<Permission> administrationPermissions() {
        return EnumSet.of(ADMINISTRATOR, SUPER_ADMINISTRATOR);
    }
}
