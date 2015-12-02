package com.pocketserver.example.permissions.storage;

import java.util.Collection;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public interface PermissionLoader {
    Marker PERMISSION_IO_MARKER = MarkerFactory.getMarker("PERMISSION_IO");

    Collection<String> get(String username);
}
