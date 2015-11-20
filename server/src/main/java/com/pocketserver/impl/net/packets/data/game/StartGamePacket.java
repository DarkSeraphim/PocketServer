package com.pocketserver.impl.net.packets.data.game;

import com.pocketserver.impl.net.Packet;
import com.pocketserver.impl.net.PacketID;
import com.pocketserver.impl.net.packets.data.DataPacket;
import com.pocketserver.impl.net.packets.data.login.LoginStatusPacket;
import com.pocketserver.impl.net.packets.udp.EncapsulatedPacket;
import com.pocketserver.player.GameMode;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

import java.net.InetSocketAddress;

@DataPacket
@PacketID(0x87)
public class StartGamePacket extends EncapsulatedPacket {
    private final int levelSeed;
    private final int unknown;
    private final int gamemode;
    private final int entityId;
    private final float x;
    private final float y;
    private final float z;

    public StartGamePacket(int levelSeed, int unknown, int gamemode, int entityId
            , float x, float y, float z) {
        this.levelSeed = levelSeed;
        this.unknown = unknown;
        this.gamemode = gamemode;
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public StartGamePacket(int levelSeed, int unknown, GameMode gamemode, int entityId
            , float x, float y, float z) {
        this.levelSeed = levelSeed;
        this.unknown = unknown;
        this.gamemode = gamemode.getId();
        this.entityId = entityId;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        ByteBuf content = dg.content();
        content.writeByte(getPacketID());
        content.writeInt(levelSeed);
        content.writeInt(unknown);
        content.writeInt(gamemode);
        content.writeInt(entityId);
        content.writeFloat(x);
        content.writeFloat(y);
        content.writeFloat(z);
        return dg;
    }

    @Override
    public Packet sendPacket(ChannelHandlerContext ctx, InetSocketAddress address) {
        new LoginStatusPacket(LoginStatusPacket.StatusCode.GOOD).sendPacket(ctx,address);
        Packet packet = super.sendPacket(ctx, address);
        new TimePacket(0).sendPacket(ctx,address);
        return packet;
    }
}
