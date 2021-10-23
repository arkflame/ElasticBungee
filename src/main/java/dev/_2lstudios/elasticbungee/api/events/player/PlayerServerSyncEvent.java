package dev._2lstudios.elasticbungee.api.events.player;

import dev._2lstudios.elasticbungee.api.events.ElasticPlayerEvent;

public class PlayerServerSyncEvent extends ElasticPlayerEvent {

    private final String server;

    public PlayerServerSyncEvent(final String proxyID, final String username, final String server) {
        super(proxyID, username);

        this.server = server;
    }

    public String getServer() {
        return this.server;
    }
}
