package com.pocketserver.impl.net.packets.data.game;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;
import com.pocketserver.impl.net.packets.data.DataPacket;
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
    public DatagramPacket encode(DatagramPacket dg) {
        ByteBuf content = dg.content();
        content.writeByte(getPacketID());
        content.writeInt(time);
        return dg;
    }
}
