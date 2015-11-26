package com.pocketserver.net.packets.login;

import com.pocketserver.net.PacketID;
import com.pocketserver.net.packets.udp.EncapsulatedPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

@PacketID(0x10)
public class ServerHandshakePacket extends EncapsulatedPacket {
    private static final InetSocketAddress LOCAL_ADDRESS = new InetSocketAddress("127.0.0.1",0);
    private static final InetSocketAddress SYSTEM_ADDRESS = new InetSocketAddress("0.0.0.0",0);

    private final long timeStamp;
    private final long serverTimeStamp;

    public ServerHandshakePacket(long timeStamp) {
        this.timeStamp = timeStamp;
        this.serverTimeStamp = timeStamp+1000L;  //When ya just don't care anymore
    }

    @Override
    public void encode(ByteBuf content) {
        content.writeShort(96*8);

        content.writeByte(getPacketID());
        content.writeBytes(writeAddress(getRemote()));
        content.writeShort(0);
        content.writeBytes(writeAddress(LOCAL_ADDRESS));
        for (int i = 0; i < 9; i++) {
            content.writeBytes(writeAddress(SYSTEM_ADDRESS));
        }
        content.writeLong(timeStamp);
        content.writeLong(serverTimeStamp);
    }

    private byte[] writeAddress(InetSocketAddress systemAddress) {
        return putAddress(systemAddress.getHostName(),systemAddress.getPort(),(byte)4).array();
    }

    private ByteBuf putAddress(String addr, int port, byte version){
        if(!addr.contains(Pattern.quote("."))){
            try {
                addr = InetAddress.getByName(addr).getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        ByteBuf byteBuf = Unpooled.buffer(7);
        byteBuf.writeByte(version);
        if(version == 4) {
            for (String section : addr.split(Pattern.quote("."))){
                byteBuf.writeByte((byte) ((byte) ~(Integer.parseInt(section)) & 0xFF));
            }
            byteBuf.writeShort((short) port);
        }
        return byteBuf;
    }
}
