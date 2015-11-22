package com.pocketserver;

public interface Permissible {

    boolean hasPermission(String permission);

    void setPermission(String permission, boolean value);

    boolean isOp();

    void setOp(boolean op);
}
