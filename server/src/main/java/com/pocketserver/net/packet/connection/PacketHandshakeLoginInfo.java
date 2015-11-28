package com.pocketserver.net.packet.connection;

import com.pocketserver.net.Packet;
import io.netty.buffer.ByteBuf;

public class PacketHandshakeLoginInfo extends Packet {
    private String username;

    // TODO: https://github.com/NiclasOlofsson/MiNET/blob/master/src/MiNET/MiNET/Net/MCPE%20Protocol%20Documentation.md#package-connection-request-accepted-0x10
    @Override
    public void read(ByteBuf buf) {

    }
}
