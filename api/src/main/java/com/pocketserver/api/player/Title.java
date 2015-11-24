package com.pocketserver.api.player;

public class Title {
    private final String topLine; // TODO RENAME THESE
    private final String bottomLine;
    private final short fadeDelay;

    public Title(String topLine, String bottomLine, short fadeDelay) {
        this.topLine = topLine;
        this.bottomLine = bottomLine;
        this.fadeDelay = fadeDelay;
    }

    public String getTopLine() {
        return topLine;
    }

    public String getBottomLine() {
        return bottomLine;
    }

    public short getFadeDelay() {
        return fadeDelay;
    }

    public void send(Player player) {
        // TODO: How do we send these?
        // TitlePacket packet = new TitlePacket();
        // packet.encode(new DatagramPacket(Unpooled.buffer(),
        // player.getAddress()));
    }
}
