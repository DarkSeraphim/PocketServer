package com.pocketserver.net.packets.data.game;

import com.pocketserver.api.player.GameMode;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.packets.data.DataPacket;
import com.pocketserver.net.packets.udp.EncapsulatedPacket;
import io.netty.buffer.ByteBuf;

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
    public void encode(ByteBuf content) {
        super.encode(content);
        content.writeByte(getPacketID());
        content.writeInt(levelSeed);
        content.writeInt(unknown);
        content.writeInt(gamemode);
        content.writeInt(entityId);
        content.writeFloat(x);
        content.writeFloat(y);
        content.writeFloat(z);
    }

    /*
    @Override
    public Packet sendPacket(ChannelHandlerContext ctx, InetSocketAddress address) {
        new LoginStatusPacket(LoginStatusPacket.StatusCode.GOOD).sendPacket(ctx,address);
        Packet packet = super.sendPacket(ctx, address);
        new TimePacket(0).sendPacket(ctx,address);
        return packet;
    }
    */
}
