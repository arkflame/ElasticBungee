package dev._2lstudios.elasticbungee.sync.results;

public class PlayerSyncResult {

    private String proxyID;
    private String serverName;
    private String address;

    public PlayerSyncResult(final String proxyID, final String serverName, final String address) {
        this.proxyID = proxyID;
        this.serverName = serverName;
        this.address = address;
    }

    public String getProxyID() {
        return this.proxyID;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getAddress() {
        return this.address;
    }

    @Override
    public String toString() {
        return this.proxyID + ":" + this.serverName + ":" + this.address;
    }

    public static PlayerSyncResult fromString(final String raw) {
        final String[] parts = raw.split(":");
        return new PlayerSyncResult(parts[0], parts[1], parts[2]);
    }
}
