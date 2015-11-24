package com.pocketserver.net.packets.data.login;

import com.pocketserver.net.Packet;
import com.pocketserver.net.PacketID;
import com.pocketserver.net.packets.data.DataPacket;
import io.netty.channel.socket.DatagramPacket;

@DataPacket
@PacketID(0x83)
public class LoginStatusPacket extends Packet {
    private final int statusCode;

    public LoginStatusPacket(int statusCode) {
        this.statusCode = statusCode;
    }

    public LoginStatusPacket(StatusCode statusCode) {
        this.statusCode = statusCode.code;
    }

    @Override
    public DatagramPacket encode(DatagramPacket dg) {
        dg.content().writeInt(getPacketID());
        dg.content().writeInt(statusCode);
        return dg;
    }

    public enum StatusCode {
        GOOD(0),
        SERVER_OUTDATED(1),
        GAME_OUTDATED(2);

        private final int code;

        StatusCode(int code) {
            this.code = code;
        }
    }
}
