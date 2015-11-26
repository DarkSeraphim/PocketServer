package com.pocketserver.net.packets.login;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import io.netty.buffer.ByteBuf;

@PacketID(0x15)
public class ClientCancelConnectPacket extends Packet {
    @Override
    public void decode(ByteBuf content) {
        System.out.println(getRemote().getHostName() + " has cancelled the login.");
    }
}
