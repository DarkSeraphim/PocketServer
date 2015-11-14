package com.pocketserver.impl.net.packets.login;

import com.pocketserver.impl.net.OutPacket;
import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.regex.Pattern;

@PacketID(0x10)
public class ServerHandshakePacket extends OutPacket {
    private static final InetSocketAddress LOCAL_ADDRESS = new InetSocketAddress("127.0.0.1",0);
    private static final InetSocketAddress SYSTEM_ADDRESS = new InetSocketAddress("0.0.0.0",0);

    private final long timeStamp;
    private final long serverTimeStamp;

    public ServerHandshakePacket(long timeStamp) {
        this.timeStamp = timeStamp;
        serverTimeStamp = timeStamp+50L;  //When ya just don't care anymore
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        ByteBuf content = dg.content();
        content.writeByte(getPacketID());
        content.writeShort(0);

        content.writeBytes(writeAddress(dg.recipient()));
        content.writeBytes(writeAddress(LOCAL_ADDRESS));
        for (int i = 0; i < 9; i++) {
            content.writeBytes(writeAddress(SYSTEM_ADDRESS));
        }
        content.writeLong(timeStamp);
        content.writeLong(serverTimeStamp);
        return dg;
    }

    private byte[] writeAddress(InetSocketAddress systemAddress) {
        return putAddress(systemAddress.getHostName(),systemAddress.getPort(),(byte)4).array();
    }

    protected ByteBuf putAddress(String addr, int port, byte version){
        if(!addr.contains(Pattern.quote("."))){
            try {
                addr = InetAddress.getByName(addr).getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        ByteBuf byteBuf = Unpooled.buffer(7);
        byteBuf.writeByte(version);
        if(version == 4){
            for (String section : addr.split(Pattern.quote("."))){
                byteBuf.writeByte((byte) ((byte) ~(Integer.parseInt(section)) & 0xFF));
            }
            byteBuf.writeShort((short) port);
        }
        System.out.println(byteBuf.readableBytes());
        return byteBuf;
    }

    @Override
    public Packet sendPacket(ChannelHandlerContext ctx, InetSocketAddress sentTo) {
        DatagramPacket encode = encode(new DatagramPacket(Unpooled.buffer(96), sentTo));
        ByteBuf encodedBuf = encode.content();
        System.out.println(encodedBuf.readableBytes());

        DatagramPacket encapsulated = new DatagramPacket(Unpooled.buffer(99),sentTo);
        ByteBuf content = encapsulated.content();
        content.writeByte(0x00);
        content.writeShort(encodedBuf.readableBytes());
        content.writeBytes(encodedBuf);
        ctx.writeAndFlush(encapsulated);
        System.out.println("Adios new packet.");
        System.out.println(encodedBuf.array().length);
        System.out.println(Arrays.toString(encodedBuf.array()));
        return this;
    }
}
