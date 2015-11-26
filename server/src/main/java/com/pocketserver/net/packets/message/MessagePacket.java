package com.pocketserver.net.packets.message;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import io.netty.buffer.ByteBuf;

@PacketID(0x85)
public class MessagePacket extends Packet {
    private String message;

    public MessagePacket(String message) {
        this.message = message;
    }

    public MessagePacket() {
    } // no-args for decoding

    @Override
    public void decode(ByteBuf content) {
        System.out.println("Message Packet.");
    }
}
