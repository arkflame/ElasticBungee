package dev._2lstudios.elasticbungee.sync.results;

public class OnlineCountSyncResult {

    private int playerCount;
    private long lastUpdate;

    public OnlineCountSyncResult(final int playerCount) {
        this.playerCount = playerCount;
        this.updateLastUpdate();
    }

    public void updateLastUpdate() {
        this.lastUpdate = System.currentTimeMillis();
    }

    public void setPlayerCount(int newValue) {
        this.playerCount = newValue;
        this.updateLastUpdate();
    }

    public void addPlayer() {
        this.setPlayerCount(this.getPlayerCount() + 1);
    }

    public void removePlayer() {
        this.setPlayerCount(this.getPlayerCount() - 1);
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public int getLastUpdateSeconds() {
        return (int) (System.currentTimeMillis() - this.lastUpdate / 1000);
    }
}
