package com.pocketserver.net;

import com.google.common.base.Charsets;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.net.InetAddresses;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;

import com.pocketserver.api.Server;
import com.pocketserver.api.util.PocketLogging;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ResourceLeak;
import io.netty.util.ResourceLeakDetector;

public abstract class Packet {
    private static final ResourceLeakDetector<Packet> leakDetector = new ResourceLeakDetector<>(Packet.class);

    private final ResourceLeak leak;

    private final PacketRegistry.PacketType type;

    protected Packet(PacketRegistry.PacketType type) {
        this.leak = leakDetector.open(this);
        this.type = type;
        Server.getServer().getLogger().trace(PocketLogging.Server.NETWORK, "Instantiating new {}", getClass().getCanonicalName());
    }

    public PacketRegistry.PacketType getType() {
        return this.type;
    }

    public void handle(ChannelHandlerContext ctx, List<Packet> out) throws Exception {

    }

    public void write(ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("packet should implement write");
    }

    public void read(ByteBuf buf) throws Exception {
        throw new UnsupportedOperationException("packet should implement read");
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(Packet.class)
            .add("id", String.format("0x%02X", PacketRegistry.getId(this)))
            .add("type", getClass().getSimpleName())
            .toString();
    }

    protected final void writeMagic(ByteBuf buf) {
        Preconditions.checkArgument(buf.isWritable(), "unable to write to buf");
        buf.writeLong(Protocol.MAGIC_1);
        buf.writeLong(Protocol.MAGIC_2);
    }

    protected final String readString(ByteBuf buf) {
        Preconditions.checkArgument(buf.isReadable(), "unable to read from buf");
        byte[] data = new byte[buf.readShort()];
        buf.readBytes(data);
        return new String(data, Charsets.UTF_8);
    }

    protected final void writeString(ByteBuf buf, String str) {
        Preconditions.checkArgument(buf.isWritable(), "unable to write to buf");
        str = Protocol.DISALLOWED_CHARS.matcher(str).replaceAll("");
        byte[] data = str.getBytes(Charsets.UTF_8);
        buf.writeShort(data.length);
        buf.writeBytes(data);
    }

    protected final void writeAddress(ByteBuf buf, InetSocketAddress address) {
        InetAddress addr;
        if (address.isUnresolved()) {
            try {
                addr = InetAddress.getByName(address.getHostName());
            } catch (UnknownHostException e) {
                return;
            }
        } else {
            addr = address.getAddress();
        }

        if (addr.getClass() == Inet6Address.class) {
            Inet6Address temp = (Inet6Address) addr;
            try {
                addr = InetAddresses.getEmbeddedIPv4ClientAddress(temp);
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException("address type not supported", ex);
            }
        }

        buf.writeByte(0x04);
        for (byte b : addr.getAddress()) {
            buf.writeByte(~b & 0xFF);
        }
        buf.writeShort(address.getPort());
    }

    protected final InetSocketAddress readAddress(ByteBuf buf) {
        Preconditions.checkArgument(buf.readByte() == 0x04, "address type not supported");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(~(buf.readByte() | ~0xFF));
            builder.append(".");
        }
        builder.setLength(builder.length() - 1);
        short port = buf.readShort();
        return new InetSocketAddress(builder.toString(), port);
    }

    public final void record(Object hint) {
        if (leak != null) {
            leak.record(hint);
        }
    }

    public final void close() {
        if (leak != null) {
            leak.close();
        }
    }
}
