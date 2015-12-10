package com.pocketserver.net.packet.raknet;

import com.google.common.base.Preconditions;

import java.net.InetSocketAddress;
import java.util.List;

import com.pocketserver.PocketServer;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

public class PacketRaknetAck extends Packet {
    private final int[] packets;

    public PacketRaknetAck(int... packets) {
        this(PacketRegistry.DefaultPacketType.ACK, packets);
    }

    public PacketRaknetAck(PacketRegistry.DefaultPacketType type, int... packets) {
        super(type);
        Preconditions.checkArgument(packets.length > 0, "number of acknowledged packets must be greater than zero");
        this.packets = packets;
    }

    public PacketRaknetAck() {
        this(new int[0]);
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        //Doesn't necessarily need to decode anything.
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        InetSocketAddress address = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get();
        PocketServer.getServer().getLogger().debug(PocketLogging.Server.NETWORK, String.format(
                "Received a %s from %s.", getClass().getSimpleName(), address.getHostName()));
    }

    @Override
    public void write(ByteBuf buf) {
        ByteBuf payload = Unpooled.buffer(2048);
        int count = packets.length;
        int records = 0;

        if (count > 0) {
            int pointer = 0;
            int start = packets[0];
            int last = packets[0];

            while (pointer + 1 < count) {
                int current = packets[pointer++];
                int diff = current - last;
                if (diff == 1) {
                    last = current;
                } else if (diff > 1) {
                    if (start == last) {
                        payload.writeByte((byte) 0x01);
                        payload.writeMedium(start);
                        start = last = current;
                    } else {
                        payload.writeByte((byte) 0x00);
                        payload.writeMedium(start);
                        payload.writeMedium(last);
                        start = last = current;
                    }
                    records = records + 1;
                }
            }

            if (start == last) {
                payload.writeByte((byte) 0x01);
                payload.writeMedium(start);
            } else {
                payload.writeByte((byte) 0x00);
                payload.writeMedium(start);
                payload.writeMedium(last);
            }
            records = records + 1;
        }
        buf.writeShort(records);
        buf.writeBytes(payload);
    }
}
