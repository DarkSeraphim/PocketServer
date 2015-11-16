package com.pocketserver.impl.net.packets.login;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;

import com.pocketserver.impl.net.PacketManager;
import com.pocketserver.impl.net.packets.udp.CustomPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

@PacketID(0x13)
public class ClientHandshakePacket extends InPacket {

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        System.out.println("Received next packet with length, " + dg.content().readableBytes());
//        if (dg.content().readInt() != COOKIE && dg.content().readByte() != SECURITY)
//            return;
        getAddress(dg.content());
        for(int i = 0; i < 10; i++){
            getAddress(dg.content());
        }
        dg.content().readLong();
        dg.content().readLong();

        System.out.println(dg.content().readableBytes());
        byte b = dg.content().readByte();
        CustomPacket.EncapsulationStrategy strategy = CustomPacket.EncapsulationStrategy.getById(b);
        if (strategy != null) {
            dg.content().readShort();
            strategy.decode(ctx,dg);
        }
    }

    private void getAddress(ByteBuf buf) {
        int version = buf.readByte();
        if(version == 4){
            buf.readByte();
            buf.readByte();
            buf.readByte();
            buf.readByte();
            buf.readShort();
        }
    }
}
