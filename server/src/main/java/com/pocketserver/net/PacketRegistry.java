package com.pocketserver.net;

import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Map.Entry;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.packet.connect.PacketConnectOpenNewConnection;
import com.pocketserver.net.packet.connect.PacketConnectOpenRequest;
import com.pocketserver.net.packet.connect.PacketConnectOpenResponse;
import com.pocketserver.net.packet.handshake.PacketHandshakeLogin;
import com.pocketserver.net.packet.handshake.PacketHandshakePlayerStatus;
import com.pocketserver.net.packet.notify.PacketNotifyDisconnect;
import com.pocketserver.net.packet.notify.PacketNotifyNoFreeConnections;
import com.pocketserver.net.packet.ping.PacketPingConnectedPing;
import com.pocketserver.net.packet.ping.PacketPingConnectedPong;
import com.pocketserver.net.packet.ping.PacketPingUnconnectedPing;
import com.pocketserver.net.packet.ping.PacketPingUnconnectedPong;
import com.pocketserver.net.packet.play.PacketPlayBatch;
import com.pocketserver.net.packet.play.PacketPlayDisconnect;
import com.pocketserver.net.packet.play.PacketPlayRemoveEntity;
import com.pocketserver.net.packet.play.PacketPlayRemovePlayer;
import com.pocketserver.net.packet.play.PacketPlaySetTime;
import com.pocketserver.net.packet.play.PacketPlaySpawnExperience;
import com.pocketserver.net.packet.play.PacketPlayStartGame;
import com.pocketserver.net.packet.play.PacketPlayText;
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

        register(0x09, PacketConnectOpenRequest.class);
        register(0x10, PacketConnectOpenResponse.class);
        register(0x13, PacketConnectOpenNewConnection.class);

        register(0x14, PacketNotifyNoFreeConnections.class);
        register(0x15, PacketNotifyDisconnect.class);
        register(0x17, PacketNotifyDisconnect.class);

        register(0x8F, PacketHandshakeLogin.class);
        register(0x90, PacketHandshakePlayerStatus.class);

        register(0x91, PacketPlayDisconnect.class);
        register(0x92, PacketPlayBatch.class);
        register(0x93, PacketPlayText.class);
        register(0x94, PacketPlaySetTime.class);
        register(0x95, PacketPlayStartGame.class);

        register(0x97, PacketPlayRemovePlayer.class);
        register(0x99, PacketPlayRemoveEntity.class);

        register(0xC5, PacketPlaySpawnExperience.class);


        register(0x05, PacketRaknetOpenConnectionRequestA.class);
        register(0x06, PacketRaknetOpenConnectionReplyA.class);
        register(0x07, PacketRaknetOpenConnectionRequestB.class);
        register(0x08, PacketRaknetOpenConnectionReplyB.class);
        register(0x1A, PacketRaknetIncompatibleProtocol.class);
        register(0xA0, PacketRaknetNack.class);
        register(0xC0, PacketRaknetAck.class);

        // TODO: Investigate whether getId may have unintended side effects when used on PacketRaknetCustom
        for (int i = 0x80; i <= 0x8F; i++) {
            register(i, PacketRaknetCustom.class);
        }
    }

    private PacketRegistry() {
        throw new UnsupportedOperationException("PacketRegistry cannot be instantiated!");
    }

    public static Packet construct(byte id) throws ReflectiveOperationException {
        Class<? extends Packet> clazz;
        if ((clazz = packetMap.get(id)) != null) {
            Constructor<? extends Packet> constructor = constructors.getIfPresent(clazz);
            if (constructor == null) {
                Server.getServer().getLogger().trace(PocketLogging.Server.NETWORK, "Caching no-args constructor for {}", clazz.getCanonicalName());
                constructor = clazz.getConstructor();
                constructors.put(clazz, constructor);
            }
            return constructor.newInstance();
        }
        throw new IllegalArgumentException("A packet with that ID does not exist!");
    }

    public static byte getId(Packet packet) {
        return getId(packet.getClass());
    }

    public static byte getId(Class<? extends Packet> clazz) {
        for (Entry<Byte, Class<? extends Packet>> entry : packetMap.entrySet()) {
            if (entry.getValue() == clazz) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Packet type has not been registered!");
    }

    private static void register(int id, Class<? extends Packet> clazz) {
        Preconditions.checkNotNull(clazz, "clazz should not be null!");
        packetMap.putIfAbsent((byte) id, clazz);
    }
}