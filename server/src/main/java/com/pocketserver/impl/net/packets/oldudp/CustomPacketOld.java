package com.pocketserver.impl.net.packets.oldudp;

import com.google.common.base.Preconditions;
import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;

import com.pocketserver.impl.net.PacketManager;
import com.pocketserver.impl.net.packets.udp.ACKPacket;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID({ 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x86, 0x87, 0x88, 0x89, 0x8A, 0x8B, 0x8C, 0x8D, 0x8E, 0x8F })
public class CustomPacketOld extends Packet {

    public enum EncapsulationStrategy {
        BARE(0x00, false, false), COUNT(0x40, true, false), COUNT_UNKNOWN(0x60, true, true);

        private int id;
        private boolean count, unknown;

        EncapsulationStrategy(int id, boolean count, boolean unknown) {
            this.id = id;
            this.count = count;
            this.unknown = unknown;
        }

        public static EncapsulationStrategy get(int id) {
            for (EncapsulationStrategy st : values())
                if (st.id == id)
                    return st;
            return null;
        }

    }

    int packetId, cap_count, cap_unknown, num;
    EncapsulationStrategy strategy;
    Packet packet = null;

    public CustomPacketOld(int packetId, EncapsulationStrategy strategy, int cap_count, int cap_unknown, Packet packet) {
        Preconditions.checkArgument(0x80 <= packetId && packetId <= 0x8F, "Packet ID must be in range 0x80 to 0x8F for custom packets.");
        Preconditions.checkNotNull(packet, "Packet cannot be null.");
        this.packetId = packetId;
        this.strategy = strategy;
        this.cap_count = cap_count;
        this.cap_unknown = cap_unknown;
        this.packet = packet;
    }

    public static CustomPacketOld newBarePacket(int packetId, Packet packet) {
        return new CustomPacketOld(packetId, EncapsulationStrategy.BARE, 0, 0, packet);
    }

    public static CustomPacketOld newCountPacket(int packetId, int cap_count, Packet packet) {
        return new CustomPacketOld(packetId, EncapsulationStrategy.COUNT, cap_count, 0, packet);
    }

    public static CustomPacketOld newCountUnknownPacket(int packetId, int cap_count, int cap_unknown, Packet packet) {
        return new CustomPacketOld(packetId, EncapsulationStrategy.COUNT_UNKNOWN, cap_count, cap_unknown, packet);
    }

    public CustomPacketOld() {
    } // no-args for decoding

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        /*
         * while (dg.content().readableBytes() > 0) { String s =
         * Integer.toHexString(dg.content().readByte()).toUpperCase(); if
         * (s.length() < 2) s = "0" + s; System.out.print((s.length() > 2 ?
         * s.substring(s.length() - 2, s.length()) : s) + " "); }
         * System.out.println();
         */
        num = dg.content().readMedium();
        byte encapsulation = dg.content().readByte();
        short packet_bits = dg.content().readShort();
        short packet_bytes = (short) (packet_bits / 8);
        strategy = EncapsulationStrategy.get(encapsulation);
        System.out.format("Received custom packet %X, num = %d, strat = %X, bytes = %d\n", getPacketID(), num, encapsulation, dg.content().readableBytes());
        if (strategy != null) {
            cap_count = strategy.count ? dg.content().readMedium() : 0;
            cap_unknown = strategy.unknown ? dg.content().readInt() : 0;
            byte packet_id = dg.content().readByte();
            byte[] packet_data = new byte[packet_bytes + 1];
            packet_data[0] = packet_id;
            dg.content().readBytes(packet_data, 1, packet_bytes - 1);
            packet = PacketManager.getInstance().initializePacketById(packet_id);
            System.out.format("Received game packet ID 0x%s", packet_id < 10 ? "0" + String.format("%X", packet_id) : String.format("%X", packet_id));
            if (packet != null) {
                System.out.format(", type = %s\n", packet.getClass().getSimpleName());
                packet.decode(new DatagramPacket(Unpooled.copiedBuffer(packet_data).readerIndex(1), dg.recipient(), dg.sender()), ctx);
            } else {
                System.out.println();
            }
            new ACKPacket(new int[]{cap_count}).sendPacket(ctx,dg.sender());
           // AcknowledgedPacketOld.one(1, num).sendPacket(ctx, dg.sender());
        }
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        //PacketManager.getInstance().save(packet);
        dg.content().writeByte(packetId);
        // dg.content().writeMedium(PacketManager.getInstance().getSentAmount());
        dg.content().writeMedium(0);
        dg.content().writeByte(strategy.id);
        if (strategy.count) {
            dg.content().writeMedium(cap_count);
        }
        if (strategy.unknown) {
            dg.content().writeInt(cap_unknown);
        }
        DatagramPacket p = packet.encode(new DatagramPacket(Unpooled.buffer(), dg.recipient(), dg.sender()));
        dg.content().writeShort(p.content().writerIndex());
        dg.content().writeBytes(p.content());
        return dg;
    }

}
