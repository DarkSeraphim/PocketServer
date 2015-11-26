package com.pocketserver.net.packets.login.connect;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@PacketID(0x07)
public class OpenConnectionRequestBPacket extends Packet {
    private long clientId;
    private int cookie;
    private short port;
    private short mtu;
    private byte sec;

    @Override
    public void decode(ByteBuf content) {
        if (content.readLong() == Protocol.MAGIC_1 && content.readLong() == Protocol.MAGIC_2) {
            sec = content.readByte();
            cookie = content.readInt();
            port = content.readShort();
            mtu = content.readShort();
            clientId = content.readLong();
        }
    }

    @Override
    public void handlePacket(Channel channel) {
        OpenConnectionReplyBPacket packet = new OpenConnectionReplyBPacket(mtu);
        packet.setRemote(getRemote());
    }
}
