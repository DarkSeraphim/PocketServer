package com.pocketserver.impl.net.packets.message;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;
import com.pocketserver.impl.net.util.PacketUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

@PacketID(0xB5)
public class ChatPacket extends Packet {
    String message;

    @Override
    public void decode(DatagramPacket dg, ChannelHandlerContext ctx) {
        message = PacketUtils.readString(dg.content());
        /*
         * Player player = PlayerRegistry.get().getPlayer(dg.sender()); if
         * (player == null) return; PlayerChatEvent event = new
         * PlayerChatEvent(player, message);
         * PocketServer.getServer().getEventBus().post(event); if
         * (event.isCancelled()) return; player.chat(event.getMessage());
         */
    }
}
