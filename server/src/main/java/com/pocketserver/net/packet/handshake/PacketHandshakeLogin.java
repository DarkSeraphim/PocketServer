package com.pocketserver.net.packet.handshake;

import java.util.UUID;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.player.PocketSkin;
import io.netty.buffer.ByteBuf;

public class PacketHandshakeLogin extends Packet {
    private PocketSkin skin;
    private String username;
    private int protocolOne;
    private int protocolTwo;
    private long clientId;
    private UUID uniqueId;
    private String host;

    @Override
    public void read(ByteBuf buf) throws Exception {
        try {
            username = readString(buf);
            protocolOne = buf.readInt();
            protocolTwo = buf.readInt();
            clientId = buf.readLong();
            byte[] idBytes = new byte[16];
            buf.readBytes(idBytes, 0, 16);
            uniqueId = UUID.nameUUIDFromBytes(idBytes);
            host = readString(buf);

            byte alpha = buf.readByte();
            boolean slim = buf.readBoolean();
            byte[] data = new byte[buf.readShort()];
            buf.readBytes(data);

            skin = new PocketSkin();
            skin.setAlpha(alpha);
            skin.setSlim(slim);
            skin.setData(data);
        } finally {
            Server.getServer().getLogger().info(PocketLogging.Server.NETWORK, "{} is attempting to login", username);
        }
    }
}
