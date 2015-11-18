package com.pocketserver.impl.net.packets.udp;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;
import com.pocketserver.impl.net.PacketManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID({ 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x86, 0x87, 0x88, 0x89, 0x8A, 0x8B, 0x8C, 0x8D, 0x8E, 0x8F })
public class CustomPacket extends InPacket {

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        ByteBuf content = dg.content();

        int counter = content.readMedium();
        byte encapsulation = content.readByte();
        short packetBits = content.readShort();
        short packetBytes = (short) (packetBits / 8);

        new ACKPacket(new int[]{counter}).sendPacket(ctx, dg.sender());

        EncapsulationStrategy strategy = EncapsulationStrategy.getById(encapsulation);
        DatagramPacket packet = new DatagramPacket(content,dg.recipient(),dg.sender());
        if (strategy != null) {
            strategy.decode(ctx, packet);
        } else {
            System.out.println("Houston, we have a problem.");
        }
    }

    public enum EncapsulationStrategy {
        BARE(0x00) {
            @Override
            public void decode(ChannelHandlerContext ctx, DatagramPacket packet) {
                ByteBuf content = packet.content();

                byte id = content.readByte();

                String sid = String.format("%X", id);
                System.out.format("Encapsulated PacketID received: 0x%s\n", sid.length() == 1 ? "0" + sid : sid);
                Packet initialized;
                try {
                    initialized = PacketManager.getInstance().initializePacketById(id);
                } catch (Exception e) {
                    e.getSuppressed();
                    initialized = PacketManager.getInstance().initializeDataPacketById(id);

                }
                System.out.println("Received encapsulated packet: " + initialized.getClass().getSimpleName());
                DatagramPacket send = new DatagramPacket(content.readBytes(content.readableBytes()), packet.recipient(), packet.sender());
                initialized.decode(send,ctx);
            }
        },
        COUNT(0x40) {
            @Override
            public void decode(ChannelHandlerContext ctx, DatagramPacket packet) {
                packet.content().readBytes(3);
                BARE.decode(ctx, packet);
            }
        },
        COUNT_UNKNOWN(0x60) {
            @Override
            public void decode(ChannelHandlerContext ctx, DatagramPacket packet) {
                packet.content().readBytes(4);
                COUNT.decode(ctx, packet);
            }
        };

        private final int id;

        EncapsulationStrategy(int id) {
            this.id = id;
        }

        public static EncapsulationStrategy getById(int id) {
            for (EncapsulationStrategy encapsulationStrategy : values())
                if (encapsulationStrategy.id == id) return encapsulationStrategy;
            return null;
        }

        public abstract void decode(ChannelHandlerContext ctx, DatagramPacket packet);
    }
}
