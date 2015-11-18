package com.pocketserver.impl.net;

import com.google.common.base.Preconditions;
import com.pocketserver.impl.exception.InvalidPacketException;
import com.pocketserver.impl.net.packets.data.DataPacket;
import com.pocketserver.impl.net.packets.data.IPRecentlyConnected;
import com.pocketserver.impl.net.packets.login.ClientCancelConnectPacket;
import com.pocketserver.impl.net.packets.login.ClientConnectPacket;
import com.pocketserver.impl.net.packets.login.ClientHandshakePacket;
import com.pocketserver.impl.net.packets.data.LoginInfoPacket;
import com.pocketserver.impl.net.packets.login.connect.OpenConnectionRequestAPacket;
import com.pocketserver.impl.net.packets.login.connect.OpenConnectionRequestBPacket;
import com.pocketserver.impl.net.packets.login.connect.UnconnectedPingPacket;
import com.pocketserver.impl.net.packets.message.ChatPacket;
import com.pocketserver.impl.net.packets.ping.PingPacket;
import com.pocketserver.impl.net.packets.udp.ACKPacket;
import com.pocketserver.impl.net.packets.udp.CustomPacket;
import com.pocketserver.impl.net.packets.udp.NACKPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class PacketManager {

    private static final PacketManager INSTANCE = new PacketManager();

    public static PacketManager getInstance() {
        return INSTANCE;
    }

    private PacketManager() {
    }

    private final Map<Byte, Class<? extends Packet>> packetIds = new HashMap<>();
    private final Map<Byte, Class<? extends Packet>> dataPacketIds = new HashMap<>();

    {
        registerPacket(UnconnectedPingPacket.class);
        registerPacket(OpenConnectionRequestAPacket.class);
        registerPacket(OpenConnectionRequestBPacket.class);
        registerPacket(CustomPacket.class);
        registerPacket(ACKPacket.class);
        registerPacket(NACKPacket.class);

        registerPacket(PingPacket.class);
        registerPacket(ChatPacket.class);
        registerPacket(ClientConnectPacket.class);
        registerPacket(ClientHandshakePacket.class);
        registerPacket(ClientCancelConnectPacket.class);
        registerPacket(LoginInfoPacket.class);
        registerPacket(IPRecentlyConnected.class);
    }

    public void registerPacket(Class<? extends Packet> packet) {
        Preconditions.checkNotNull(packet);
        PacketID id = packet.getDeclaredAnnotation(PacketID.class);
        if (id == null) {
            throw new InvalidPacketException("All packets must be annotated with @PacketID.", packet);
        }
        for (int i : id.value()) {
            if (packet.isAnnotationPresent(DataPacket.class)) {
                this.dataPacketIds.put((byte) i, packet);
                continue;
            }
            packetIds.put((byte) i, packet);
        }
    }

    public Class<? extends Packet> getPacketById(byte id) {
        return packetIds.get(id);
    }

    public Packet initializePacketById(byte id) {
        return initializePacket(getPacketById(id));
    }

    public Packet initializePacket(Class<? extends Packet> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class<? extends Packet> getDataPacketById(byte id) {
        return dataPacketIds.get(id);
    }

    public Packet initializeDataPacketById(byte id) {
        return initializePacket(getDataPacketById(id));
    }
}