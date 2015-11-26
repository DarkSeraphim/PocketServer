package com.pocketserver.net.packets.data.game;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.packets.data.DataPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.socket.DatagramPacket;

@DataPacket
@PacketID(0x86)
public class TimePacket extends Packet {
    private final int time;

    public TimePacket(int time) {
        this.time = time;
    }

    @Override
    public void encode(ByteBuf content) {
        content.writeByte(getPacketID());
        content.writeInt(time);
    }
}
