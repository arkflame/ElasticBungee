package dev._2lstudios.elasticbungee.sync;

public class PlayerSyncResult {
    private String proxyID;
    private String serverName;

    public PlayerSyncResult(final String proxyID, final String serverName) {
        this.proxyID = proxyID;
        this.serverName = serverName;
    }

    public String getProxyID() {
        return this.proxyID;
    }

    public String getServerName() {
        return this.serverName;
    }

    @Override
    public String toString() {
        return this.proxyID + ":" + this.serverName;
    }

    public static PlayerSyncResult fromString(final String raw) {
        final String[] parts = raw.split(":");
        return new PlayerSyncResult(parts[0], parts[1]);
    }
}
