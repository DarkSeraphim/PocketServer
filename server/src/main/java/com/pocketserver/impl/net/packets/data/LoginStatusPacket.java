package com.pocketserver.impl.net.packets.data;

import com.pocketserver.impl.net.OutPacket;
import com.pocketserver.impl.net.PacketID;
import io.netty.channel.socket.DatagramPacket;

@DataPacket
@PacketID(0x83)
public class LoginStatusPacket extends OutPacket {
    private final int statusCode;

    public LoginStatusPacket(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        dg.content().writeInt(getPacketID());
        dg.content().writeInt(statusCode);
        return dg;
    }
}
