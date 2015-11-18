package com.pocketserver.impl.net.packets.login;

import com.pocketserver.impl.net.InPacket;
import com.pocketserver.impl.net.PacketID;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0x13)
public class ClientHandshakePacket extends InPacket {

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        /*
        System.out.println("Received next packet with length, " + dg.content().readableBytes());

        getAddress(dg.content());
        for(int i = 0; i < 10; i++){
            getAddress(dg.content());
        }
        dg.content().readLong();
        dg.content().readLong();

        System.out.println("Finished reading next packet: " + dg.content().readableBytes());
        dg.content().readByte();
        dg.content().readByte();
        byte b = dg.content().readByte();
        dg.content().readShort();

        CustomPacket.EncapsulationStrategy strategy = CustomPacket.EncapsulationStrategy.getById(b);

        String sid = String.format("%X", b);
        System.out.format(" 22222222 Encapsulated PacketID received: 0x%s\n", sid.length() == 1 ? "0" + sid : sid);
        strategy.decode(ctx,dg);
        */
        ByteBuf content = dg.content();
        int cookie = content.readInt();
        byte b = content.readByte();
        short port = content.readShort();
        long session = content.readLong();
        long session2 = content.readLong();
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
