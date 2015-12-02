package com.pocketserver.api.permissions;

public interface Permissible {

    boolean hasPermission(String permission);

    boolean isOp();

    void setOp(boolean op);
}
