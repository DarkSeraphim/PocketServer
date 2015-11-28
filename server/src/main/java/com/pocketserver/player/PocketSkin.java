package com.pocketserver.player;

public final class PocketSkin {
    private boolean slim;
    private byte[] data;
    private byte alpha;

    public PocketSkin() {
        data = new byte[0];
    }

    public boolean isSlim() {
        return slim;
    }

    public byte getAlpha() {
        return alpha;
    }

    public byte[] getData() {
        return data.clone();
    }

    public void setSlim(boolean slim) {
        this.slim = slim;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public void setAlpha(byte alpha) {
        this.alpha = alpha;
    }
}
