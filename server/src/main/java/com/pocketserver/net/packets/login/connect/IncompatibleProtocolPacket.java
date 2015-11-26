package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.Protocol;
import com.pocketserver.net.util.PacketUtils;
import io.netty.buffer.ByteBuf;

@PacketID(0x1A)
public class IncompatibleProtocolPacket extends Packet {
    @Override
    public void encode(ByteBuf content) {
        content.writeByte(this.getPacketID());
        content.writeByte(Protocol.RAKNET_VERSION);
        PacketUtils.writeMagic(content);
        content.writeLong(Protocol.TEMP_SERVER_ID);
    }
}
