package org.web.demounit.persists.entities;

public enum Permission {
    ONLY_READ("read"),
    ONLY_WRITE("write");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
