package com.pocketserver.net;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.pocketserver.net.packet.connect.PacketConnectOpenNewConnection;
import com.pocketserver.net.packet.connect.PacketConnectOpenRequest;
import com.pocketserver.net.packet.connect.PacketConnectOpenResponse;
import com.pocketserver.net.packet.handshake.PacketHandshakeLogin;
import com.pocketserver.net.packet.handshake.PacketHandshakePlayerStatus;
import com.pocketserver.net.packet.notify.PacketNotifyBanned;
import com.pocketserver.net.packet.notify.PacketNotifyDisconnect;
import com.pocketserver.net.packet.notify.PacketNotifyNoFreeConnections;
import com.pocketserver.net.packet.ping.PacketPingConnectedPing;
import com.pocketserver.net.packet.ping.PacketPingConnectedPong;
import com.pocketserver.net.packet.ping.PacketPingUnconnectedPing;
import com.pocketserver.net.packet.ping.PacketPingUnconnectedPong;
import com.pocketserver.net.packet.play.*;
import com.pocketserver.net.packet.raknet.*;

import java.util.Map;
import java.util.function.Supplier;

public final class PacketRegistry {

    public enum DefaultPacketType implements PacketType {
        UNCONNECTED_PING(0x01, PacketPingUnconnectedPing.class, PacketPingUnconnectedPing::new),
        UNCONNECTED_PONG(0x1C, PacketPingUnconnectedPong.class),
        CONNECTED_PING(0x00, PacketPingConnectedPing.class, PacketPingConnectedPing::new),
        CONNECTED_PONG(0x03, PacketPingConnectedPong.class),
        OPEN_REQUEST(0x09, PacketConnectOpenRequest.class, PacketConnectOpenRequest::new),
        OPEN_RESPONSE(0x10, PacketConnectOpenResponse.class),
        OPEN_NEW_CONNECTION(0x13, PacketConnectOpenNewConnection.class, PacketConnectOpenNewConnection::new),
        NO_FREE_CONNECTIONS(0x14, PacketNotifyNoFreeConnections.class, PacketNotifyNoFreeConnections::new),
        NOTIFY_DISCONNECT(0x15, PacketNotifyDisconnect.class, PacketNotifyDisconnect::new),
        // TODO: create proper class, incoming packets are practically indistinguishable
        CONNECTION_BANNED(0x17, PacketNotifyBanned.class, PacketNotifyBanned::new),
        LOGIN(0x8F, PacketHandshakeLogin.class, PacketHandshakeLogin::new),
        PLAYER_STATUS(0x90, PacketHandshakePlayerStatus.class, PacketHandshakePlayerStatus::new),
        PLAY_DISCONNECT(0x91, PacketPlayDisconnect.class),
        BATCH(0x92, PacketPlayBatch.class),
        TEXT(0x93, PacketPlayText.class),
        SET_TIME(0x94, PacketPlaySetTime.class),
        REMOVE_PLAYER(0x97, PacketPlayRemovePlayer.class),
        REMOVE_ENTITY(0x99, PacketPlayRemoveEntity.class),
        SPAWN_EXPERIENCE(0xC5, PacketPlaySpawnExperience.class),
        OPEN_CONNECTION_REQUEST_A(0x05, PacketRaknetOpenConnectionRequestA.class, PacketRaknetOpenConnectionRequestA::new),
        OPEN_CONNECTION_REPLY_A(0x06, PacketRaknetOpenConnectionReplyA.class),
        OPEN_CONNECTION_REQUEST_B(0x07, PacketRaknetOpenConnectionRequestB.class, PacketRaknetOpenConnectionRequestB::new),
        OPEN_CONNECTION_REPLY_B(0x08, PacketRaknetOpenConnectionReplyB.class),
        INCOMPATIBLE_PROTOCOL(0x1A, PacketRaknetIncompatibleProtocol.class, PacketRaknetIncompatibleProtocol::new),
        NACK(0xA0, PacketRaknetNack.class, PacketRaknetNack::new),
        ACK(0xC0, PacketRaknetAck.class, PacketRaknetAck::new),
        CUSTOM(0x80, PacketRaknetCustom.class, PacketRaknetCustom::new, 0x8F),
        UNKNOWN(-1, null)
        ;

        private final byte id;
        private final byte[] extraIds;
        private final Class<? extends Packet> cls;
        private final Supplier<? extends Packet> constructor;

        <T extends Packet> DefaultPacketType(int id, Class<T> cls) {
            this(id, cls, null);
        }

        <T extends Packet> DefaultPacketType(int id, Class<T> cls, Supplier<T> constructor) {
            this(id, cls, constructor, -1);
        }

        <T extends Packet> DefaultPacketType(int id, Class<T> cls, Supplier<T> constructor, int range) {
            this.id = (byte) id;
            this.cls = cls;
            this.constructor = constructor;
            int extra = range - id;
            if (extra > 0) {
                this.extraIds = new byte[extra];
                for (int i = 0; i < extra; i++) {
                    this.extraIds[i] = (byte) (id + i + 1);
                }
            } else {
                this.extraIds = new byte[0];
            }
        }

        @Override
        public byte getId() {
            return this.id;
        }

        @Override
        public boolean hasExtraIds() {
            return this.extraIds.length > 0;
        }

        @Override
        public byte[] getExtraIds() {
            return this.extraIds;
        }

        @Override
        public Class<? extends Packet> getPacketClass() {
            return this.cls;
        }

        @Override
        public boolean isClientPacket() {
            return this.constructor != null;
        }

        @Override
        public Packet createPacket() {
            Preconditions.checkNotNull(this.constructor, "Client tried to send a server packet");
            return this.constructor.get();
        }
    }

    private static final Map<Byte, PacketType> packetById;

    static {
        packetById = Maps.newConcurrentMap();

        for (PacketType packetType : DefaultPacketType.values()) {
            register(packetType);
        }
    }

    private PacketRegistry() {
        throw new UnsupportedOperationException("PacketRegistry cannot be instantiated!");
    }

    public static Packet construct(byte id) throws ReflectiveOperationException {
        PacketType type = packetById.getOrDefault(id, DefaultPacketType.UNKNOWN);
        if (type != DefaultPacketType.UNKNOWN) {
            return type.createPacket();
        }
        throw new IllegalArgumentException("A packet with the ID \'" + String.format("0x%02x", id) + "\' does not exist!");
    }

    public static byte getId(Packet packet) {
        return packet.getType().getId();
    }

    public static void register(PacketType type) {
        Preconditions.checkNotNull(type.getPacketClass(), "clazz should not be null!");
        packetById.putIfAbsent(type.getId(), type);
        if (type.hasExtraIds()) {
            for (byte eid : type.getExtraIds()) {
                packetById.putIfAbsent(eid, type);
            }
        }
    }
}