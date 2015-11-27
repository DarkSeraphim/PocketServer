package com.pocketserver.api.util;

public interface Callback<T> {
    void done(T val, Throwable err);
}
