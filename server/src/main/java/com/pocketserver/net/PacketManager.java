package com.pocketserver.net;

import com.google.common.base.Preconditions;
import com.pocketserver.exception.InvalidPacketException;
import com.pocketserver.net.packets.data.IPRecentlyConnected;
import com.pocketserver.net.packets.data.login.LoginInfoPacket;
import com.pocketserver.net.packets.login.ClientCancelConnectPacket;
import com.pocketserver.net.packets.login.ClientConnectPacket;
import com.pocketserver.net.packets.login.ClientHandshakePacket;
import com.pocketserver.net.packets.login.connect.OpenConnectionRequestAPacket;
import com.pocketserver.net.packets.login.connect.OpenConnectionRequestBPacket;
import com.pocketserver.net.packets.login.connect.UnconnectedPingPacket;
import com.pocketserver.net.packets.message.ChatPacket;
import com.pocketserver.net.packets.ping.PingPacket;
import com.pocketserver.net.packets.udp.ACKPacket;
import com.pocketserver.net.packets.udp.NACKPacket;

import java.util.HashMap;
import java.util.Map;

public final class PacketManager {

    private static final PacketManager INSTANCE = new PacketManager();

    public static PacketManager getInstance() {
        return INSTANCE;
    }

    private PacketManager() {
    }

    private final Map<Byte, Class<? extends Packet>> packetIds = new HashMap<>();

    {
        registerPacket(UnconnectedPingPacket.class);
        registerPacket(OpenConnectionRequestAPacket.class);
        registerPacket(OpenConnectionRequestBPacket.class);
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
}