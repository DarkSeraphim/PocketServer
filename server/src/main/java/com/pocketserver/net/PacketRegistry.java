package com.pocketserver.net;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

import java.lang.reflect.Constructor;
import java.util.Map;

import com.pocketserver.net.packet.connection.PacketClientConnect;
import com.pocketserver.net.packet.connection.PacketClientConnectCancelled;
import com.pocketserver.net.packet.connection.PacketConnectionRequest;
import com.pocketserver.net.packet.connection.PacketConnectionRequestAccepted;
import com.pocketserver.net.packet.connection.PacketHandshakeLoginInfo;
import com.pocketserver.net.packet.connection.PacketHandshakePlayerStatus;
import com.pocketserver.net.packet.connection.ping.PacketPingConnectedPing;
import com.pocketserver.net.packet.connection.ping.PacketPingConnectedPong;
import com.pocketserver.net.packet.connection.ping.PacketPingUnconnectedPing;
import com.pocketserver.net.packet.connection.ping.PacketPingUnconnectedPong;
import com.pocketserver.net.packet.connection.PacketPlayDisconnect;
import com.pocketserver.net.packet.raknet.PacketRaknetAck;
import com.pocketserver.net.packet.raknet.PacketRaknetCustom;
import com.pocketserver.net.packet.raknet.PacketRaknetIncompatibleProtocol;
import com.pocketserver.net.packet.raknet.PacketRaknetNack;
import com.pocketserver.net.packet.raknet.PacketRaknetOpenConnectionReplyA;
import com.pocketserver.net.packet.raknet.PacketRaknetOpenConnectionReplyB;
import com.pocketserver.net.packet.raknet.PacketRaknetOpenConnectionRequestA;
import com.pocketserver.net.packet.raknet.PacketRaknetOpenConnectionRequestB;

public final class PacketRegistry {
    private static final Cache<Class<? extends Packet>, Constructor<? extends Packet>> constructors;
    private static final Map<Byte, Class<? extends Packet>> packetMap;

    static {
        constructors = CacheBuilder.newBuilder().build();
        packetMap = Maps.newConcurrentMap();

        register(0x01, PacketPingUnconnectedPing.class);
        register(0x1C, PacketPingUnconnectedPong.class);
        register(0x00, PacketPingConnectedPing.class);
        register(0x03, PacketPingConnectedPong.class);

        register(0x05, PacketRaknetOpenConnectionRequestA.class);
        register(0x06, PacketRaknetOpenConnectionReplyA.class);
        register(0x07, PacketRaknetOpenConnectionRequestB.class);
        register(0x08, PacketRaknetOpenConnectionReplyB.class);
        register(0x1A, PacketRaknetIncompatibleProtocol.class);

        register(0x09, PacketConnectionRequest.class);
        register(0x10, PacketConnectionRequestAccepted.class);
        register(0x13, PacketClientConnect.class);
        register(0x15, PacketClientConnectCancelled.class);

        register(0x8F, PacketHandshakeLoginInfo.class);
        register(0x90, PacketHandshakePlayerStatus.class);
        register(0x91, PacketPlayDisconnect.class);

        register(0xA0, PacketRaknetNack.class);
        register(0xC0, PacketRaknetAck.class);
        for (int i = 0x80; i <= 0x8F; i++) {
            register(i, PacketRaknetCustom.class);
        }
    }

    public static Packet construct(byte id) throws ReflectiveOperationException {
        Class<? extends Packet> clazz;
        if ((clazz = packetMap.get(id)) != null) {
            Constructor<? extends Packet> constructor = constructors.getIfPresent(clazz);
            if (constructor == null) {
                constructor = clazz.getConstructor();
                constructors.put(clazz, constructor);
            }
            return constructor.newInstance();
        }
        throw new IllegalArgumentException("A packet with that ID does not exist!");
    }

    public static byte getId(Packet packet) {
        for (Map.Entry<Byte, Class<? extends Packet>> entry : packetMap.entrySet()) {
            if (entry.getValue() == packet.getClass()) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Packet has not been registered!");
    }

    private static void register(int id, Class<? extends Packet> clazz) {
        Preconditions.checkNotNull(clazz, "clazz should not be null!");
        packetMap.putIfAbsent((byte) id, clazz);
    }

    private PacketRegistry() {
        throw new UnsupportedOperationException("PacketRegistry cannot be instantiated!");
    }
}