package com.pocketserver.net;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.pocketserver.net.packets.udp.CustomPacket;
import com.pocketserver.player.Player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;

public abstract class Packet {
	
	protected static final long TEMP_SERVER_ID = 0x00000000372cdc9e;
	protected static final String TEMP_IDENTIFIER = "MCPE;Survival Games!;34;0.12.3;0;20";
	
	protected static final Pattern DISALLOWED_CHARS = Pattern.compile("[^" + Pattern.quote(
            " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~") + "]");
	
	protected static final long MAGIC_1 = 0x00ffff00fefefefeL;
	protected static final long MAGIC_2 = 0xfdfdfdfd12345678L;
    private final int id;

    protected Packet() {
        id = getClass().getAnnotation(PacketID.class).value()[0];
    }

    protected Packet(int id) {
        this.id = id;
    }


    public final void writeMagic(ByteBuf buf) {
		buf.writeLong(MAGIC_1);
		buf.writeLong(MAGIC_2);
	}

	public final void writeString(ByteBuf buf, String str) {
		Preconditions.checkNotNull(str, "Cannot write a null string.");
		str = DISALLOWED_CHARS.matcher(str).replaceAll("");
		buf.writeShort(str.length());
		buf.writeBytes(str.getBytes(Charset.defaultCharset()));
	}
	
	public final String readString(ByteBuf buf) {
		short len = buf.readShort();
		byte[] bytes = new byte[len];
		buf.readBytes(bytes);
		return new String(bytes, Charset.defaultCharset());
	}

    public final int getPacketID() {
        return this.id;
    }

	public Packet sendLogin(ChannelHandlerContext ctx, InetSocketAddress addr) {
		System.out.println("Sending login packet " + getClass().getSimpleName());
		ctx.writeAndFlush(encode(new DatagramPacket(Unpooled.buffer(), addr)));
		return this;
	}
    
    public Packet sendLogin(Player player) {
		return sendLogin(player.getChannelContext(), player.getAddress());
	}
    
	public Packet sendGame(int customPacketId, ChannelHandlerContext ctx, InetSocketAddress address) {
		System.out.println("Sending game packet " + getClass().getSimpleName());
		new CustomPacket(customPacketId, CustomPacket.EncapsulationStrategy.BARE, 0, 1, this).sendLogin(ctx, address);
		return this;
	}
    
    public Packet sendGame(int customPacketId, Player player) {
		return sendGame(customPacketId, player.getChannelContext(), player.getAddress());
	}

    public abstract void decode(ChannelHandlerContext ctx, DatagramPacket dg);
    public abstract DatagramPacket encode(DatagramPacket dg);
}