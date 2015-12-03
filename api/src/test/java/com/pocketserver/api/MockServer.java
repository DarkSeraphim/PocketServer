package com.pocketserver.api;

import com.google.common.collect.ImmutableList;

import java.io.File;
import java.util.Collection;
import java.util.Optional;

import com.pocketserver.api.command.CommandManager;
import com.pocketserver.api.permissions.PermissionResolver;
import com.pocketserver.api.player.Player;
import com.pocketserver.api.plugin.PluginManager;
import com.pocketserver.api.util.Pipeline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MockServer extends Server {
    private final Logger logger;

    public MockServer() {
        TestLogger.init();
        this.logger = LoggerFactory.getLogger(MockServer.class);
    }

    @Override
    public void shutdown() {
        throw new UnsupportedOperationException("not supported by mocked servers");
    }

    @Override
    public PluginManager getPluginManager() {
        throw new UnsupportedOperationException("not supported by mocked servers");
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public CommandManager getCommandManager() {
        throw new UnsupportedOperationException("not supported by mocked servers");
    }

    @Override
    public File getDirectory() {
        throw new UnsupportedOperationException("not supported by mocked servers");
    }

    @Override
    public Pipeline<PermissionResolver> getPermissionPipeline() {
        throw new UnsupportedOperationException("not supported by mocked servers");
    }

    @Override
    public Optional<Player> getPlayer(String username) {
        return Optional.empty();
    }

    @Override
    public Collection<Player> getOnlinePlayers() {
        return ImmutableList.of();
    }
}
