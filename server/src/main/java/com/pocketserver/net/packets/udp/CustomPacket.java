package com.pocketserver.net.packets.udp;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.PacketManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;

@PacketID({ 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x86, 0x87, 0x88, 0x89, 0x8A, 0x8B, 0x8C, 0x8D, 0x8E, 0x8F })
public class CustomPacket extends Packet {

    private int counter;
    private byte encapsulation;
    private ByteBuf content;

    @Override
    public void decode(ByteBuf content) {
        this.content = content.retain();
        this.counter = content.readMedium();
        this.encapsulation = content.readByte();
        content.readShort();
    }

    @Override
    public void handlePacket(Channel channel) {
        {
            ACKPacket ack = new ACKPacket(new int[]{counter});
            ack.setRemote(getRemote());
            ack.sendPacket(channel);
        }
        {
            EncapsulationStrategy strategy = EncapsulationStrategy.fromId(encapsulation);
            if (strategy != null) {
                strategy.decode(content, channel, getRemote());
            }
            content.release();
        }
    }

    private enum EncapsulationStrategy {
        BARE(0x00) {
            @Override
            protected void decode(ByteBuf content, Channel channel, InetSocketAddress remote) {
                byte id = content.readByte();
                Packet packet = PacketManager.getInstance().initializePacketById(id);
                packet.decode(content);
            }
        },
        COUNT(0x40) {
            @Override
            protected void decode(ByteBuf content, Channel channel, InetSocketAddress remote) {
                content.readBytes(3);
                BARE.decode(content, channel, remote);
            }
        },
        COUNT_UNKNOWN(0x60) {
            @Override
            protected void decode(ByteBuf content, Channel channel, InetSocketAddress remote) {
                content.readBytes(4);
                COUNT.decode(content, channel, remote);
            }
        };

        private final byte id;
        EncapsulationStrategy(int id) {
            this.id = (byte) id;
        }

        protected abstract void decode(ByteBuf content, Channel channel, InetSocketAddress remote);

        public static EncapsulationStrategy fromId(byte encapsulation) {
            for (EncapsulationStrategy encapsulationStrategy : values()) {
                if (encapsulationStrategy.id == encapsulation) return encapsulationStrategy;
            }
            return null;
        }
    }

    /*
    public enum EncapsulationStrategy {
        BARE(0x00) {
            @Override
            public void decode(ChannelHandlerContext ctx, DatagramPacket packet) {
                ByteBuf content = packet.content();
                System.out.println(name());

                byte id = content.readByte();
                String sid = String.format("%X",id);

                System.out.format("PacketID: 0x%s\n", sid.length() == 1 ? "0" + sid : sid);

                Packet initialized = PacketManager.getInstance().initializePacketById(id);
                System.out.println("Received encapsulated packet: " + initialized.getClass().getSimpleName());
                DatagramPacket send = new DatagramPacket(content.readBytes(content.readableBytes()), packet.recipient(), packet.sender());
                initialized.decode(send,ctx);
            }
        },
        COUNT(0x40) {
            @Override
            public void decode(ChannelHandlerContext ctx, DatagramPacket packet) {
                packet.content().readMedium();
                System.out.println(name());
                BARE.decode(ctx, packet);
            }
        },
        COUNT_UNKNOWN(0x60) {
            private boolean used;
            @Override
            public void decode(ChannelHandlerContext ctx, DatagramPacket packet) {
                //if (!used)
                    packet.content().readBytes(4);
                used = true; //TODO: Figure out if this is per run or per user. Guessing per user?
                System.out.println(name());
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
    */
}
