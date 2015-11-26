package com.pocketserver.api.command;

import com.pocketserver.api.permissions.Permissible;

public interface CommandExecutor extends Permissible {

    String getName();

    void sendMessage(String message);

}
