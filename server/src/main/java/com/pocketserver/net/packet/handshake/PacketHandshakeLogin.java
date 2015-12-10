package com.pocketserver.net.packet.handshake;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

import com.pocketserver.PocketServer;
import com.pocketserver.api.Server;
import com.pocketserver.api.event.player.PlayerLoginEvent;
import com.pocketserver.api.util.Callback;
import com.pocketserver.api.util.PocketLogging;
import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketRegistry;
import com.pocketserver.net.PipelineUtils;
import com.pocketserver.net.packet.play.PacketPlayDisconnect;
import com.pocketserver.player.PocketPlayer;
import com.pocketserver.player.PocketSkin;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class PacketHandshakeLogin extends Packet {
    private int protocolMajor;
    private int protocolMinor;
    private PocketSkin skin;
    private long clientId;
    private UUID uniqueId;
    private String name;
    private String host;

    public PacketHandshakeLogin() {
        super(PacketRegistry.PacketType.LOGIN);
    }

    @Override
    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {
        InetSocketAddress address = ctx.attr(PipelineUtils.ADDRESS_ATTRIBUTE).get();
        PocketServer server = (PocketServer) Server.getServer();
        if (server.getPlayer(address).isPresent()) {
            out.add(new PacketPlayDisconnect("Somebody is already logged in from that address."));
        } else {
            PocketPlayer player = new PocketPlayer(server, ctx.channel(), address, name);
            Callback<PlayerLoginEvent> callback = (event, err) -> {
                if (event.isCancelled()) {
                    player.unsafe().send(new PacketPlayDisconnect(event.getKickMessage()));
                } else {
                    Server.getServer().getLogger().info(PocketLogging.Server.NETWORK, "{} is attempting to login", new Object[]{
                        name
                    });
                    server.addPlayer(player);
                }
            };
            PlayerLoginEvent event = new PlayerLoginEvent(callback);
            server.getPluginManager().post(event);
        }
    }

    @Override
    public void read(ByteBuf buf) throws Exception {
        name = readString(buf);
        protocolMajor = buf.readInt();
        protocolMinor = buf.readInt();
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

        System.out.println("HELLO FROM LOGIN PACKET!!!!!!!!!!");
    }
}
