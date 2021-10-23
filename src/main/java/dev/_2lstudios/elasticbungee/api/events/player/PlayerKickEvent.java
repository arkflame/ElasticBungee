package dev._2lstudios.elasticbungee.api.events.player;

import dev._2lstudios.elasticbungee.api.events.ElasticPlayerEvent;

public class PlayerKickEvent extends ElasticPlayerEvent {

    private final String reason;

    public PlayerKickEvent(final String proxyID, final String username, final String reason) {
        super(proxyID, username);
        this.reason = reason;
    }

    public String getReason() {
        return this.reason;
    }
}
